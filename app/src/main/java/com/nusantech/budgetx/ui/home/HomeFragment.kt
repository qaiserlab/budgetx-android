package com.nusantech.budgetx.ui.home

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.nusantech.budgetx.R
import com.nusantech.budgetx.SplashActivity
import com.nusantech.budgetx.databinding.FragmentHomeBinding
import com.nusantech.budgetx.helpers.ApiCall
import com.nusantech.budgetx.helpers.AuthManager
import java.text.NumberFormat
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var lblHi: TextView
    private lateinit var btnLogout: Button
    private lateinit var lblMonthlyOverview: TextView

    lateinit var apiCall: ApiCall
    lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        lblHi = binding.lblHi

        val authManager = AuthManager(requireContext())
        val profileName: String = authManager.getProfileName().orEmpty()

        lblHi.setText("Hai, $profileName")

        btnLogout = binding.btnLogout

        btnLogout.setOnClickListener { logout() }

        lblMonthlyOverview = binding.lblMonthlyOverview

        apiCall = ApiCall(requireContext())
        apiCall.getMonthlyOverview({ type, total ->
            val currencySymbol: String = if (type == "Income") "(+)" else "(-)"
            val monthlyOverview = "$currencySymbol ${formatCurrency(total)}"

            lblMonthlyOverview.setText(monthlyOverview)
        }, {message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        })

        // ListView

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)

        val lsvTransaction = binding.lsvTransaction

        progressDialog.show()

        apiCall.getTransactions({ items ->
            val adapter = TransactionAdapter(requireContext(), R.layout.transaction_item, items)
            lsvTransaction.adapter = adapter
            progressDialog.dismiss()
        }, {message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        })

        return root
    }

    fun formatCurrency(value: Int): String {
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        val formattedValue = currencyFormat.format(value).replace("Rp", "").trim()
        return formattedValue
    }

    fun logout() {
        val alertDialogBuilder = AlertDialog.Builder(context)

        alertDialogBuilder.setTitle("Konfirmasi")
        alertDialogBuilder.setMessage("Apakah Anda ingin keluar dari sesi?")

        alertDialogBuilder.setPositiveButton("OK") { dialog, which ->
            val context: Context = requireContext()
            val authManager = AuthManager(context)

            authManager.logout()

            val intent: Intent = Intent(context, SplashActivity::class.java)
            startActivity(intent)

            dialog.dismiss()
            requireActivity().finish()
        }

        alertDialogBuilder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }

        var alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}