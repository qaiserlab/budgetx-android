package com.nusantech.budgetx.ui.laporan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.nusantech.budgetx.databinding.FragmentLaporanBinding

class LaporanFragment : Fragment() {

    private var _binding: FragmentLaporanBinding? = null

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



        return root
    }

    fun loadChart(incomePercentage: Int, expensePercentage: Int) {
        val htmlData = """
            <!DOCTYPE html>
            <html>
                <head>
                
                </head>
            <body>
            
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