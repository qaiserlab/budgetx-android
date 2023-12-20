package com.nusantech.budgetx.ui.home

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.nusantech.budgetx.SplashActivity
import com.nusantech.budgetx.databinding.FragmentHomeBinding
import com.nusantech.budgetx.helpers.AuthManager

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var lblHi: TextView
    private lateinit var btnLogout: Button
    private lateinit var lblMonthlyOverview: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        lblHi = binding.lblHi

        btnLogout = binding.btnLogout

        btnLogout.setOnClickListener { logout() }

        lblMonthlyOverview = binding.lblMonthlyOverview

        return root
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