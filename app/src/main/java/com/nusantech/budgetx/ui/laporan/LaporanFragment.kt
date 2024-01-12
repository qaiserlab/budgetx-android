package com.nusantech.budgetx.ui.laporan

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.nusantech.budgetx.databinding.FragmentLaporanBinding
import com.nusantech.budgetx.helpers.ApiCall
import java.text.NumberFormat
import java.util.Locale

class LaporanFragment : Fragment() {

    private var _binding: FragmentLaporanBinding? = null

    lateinit var apiCall: ApiCall
    lateinit var progressDialog: ProgressDialog

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var webView: WebView
    lateinit var txtPengeluaran: TextView
    lateinit var txtPemasukan: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLaporanBinding.inflate(inflater, container, false)
        val root: View = binding.root

        webView = binding.webView
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()

        txtPengeluaran = binding.txtPengeluaran
        txtPemasukan = binding.txtPemasukan

        apiCall = ApiCall(requireContext())
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)

        fetchReport()

        return root
    }

    fun fetchReport() {
        progressDialog.show()

        apiCall.getReport({ expenseTotal, expensePercentage, incomeTotal, incomePercentage ->
            loadChart(expensePercentage, incomePercentage)

            txtPengeluaran.setText(formatCurrency(expenseTotal))
            txtPemasukan.setText(formatCurrency(incomeTotal))

            progressDialog.dismiss()
        }, { message ->
            progressDialog.dismiss()
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        })
    }

    fun formatCurrency(value: Int): String {
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        val formattedValue = currencyFormat.format(value).replace("Rp", "").trim()
        return formattedValue
    }

    fun loadChart(expensePercentage: Int, incomePercentage: Int) {
        val htmlData = """
            <!DOCTYPE html>
            <html>
                <head>
                    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
                </head>
            <body>
                <canvas id="myChart" width="400" height="400"></canvas>
                <script>
                    var ctx = document.getElementById("myChart").getContext("2d")
                    var myChart = new Chart(ctx, {
                        type: 'doughnut',
                        data: {
                            label: ["Pengeluaran","Pemasukan"],
                            datasets: [{
                                backgroundColor: ["#F2994A","#219653"],
                                data: [$expensePercentage, $incomePercentage]
                            }]
                        }
                    })
                </script>
            </body>
            </html>
        """.trimIndent()

        webView.loadDataWithBaseURL(null, htmlData, "text/html", "UTF-8", null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}