package com.nusantech.budgetx.ui.transaksi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.nusantech.budgetx.databinding.FragmentTransaksiBinding
import java.text.NumberFormat
import java.util.Locale

class TransaksiFragment : Fragment() {

    private var _binding: FragmentTransaksiBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var jumlah: Int = 0

    lateinit var txtJumlah: TextView
    lateinit var btnCalc0: Button
    lateinit var btnCalc000: Button
    lateinit var btnCalc1: Button
    lateinit var btnCalc2: Button
    lateinit var btnCalc3: Button
    lateinit var btnCalc4: Button
    lateinit var btnCalc5: Button
    lateinit var btnCalc6: Button
    lateinit var btnCalc7: Button
    lateinit var btnCalc8: Button
    lateinit var btnCalc9: Button

    lateinit var btnCalcBs: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransaksiBinding.inflate(inflater, container, false)
        val root: View = binding.root

        txtJumlah = binding.txtJumlah
        txtJumlah.setText(formatCurrency(jumlah))

        btnCalc0 = binding.btnCalc0
        btnCalc0.setOnClickListener { calcJumlah("0") }

        btnCalc000 = binding.btnCalc000
        btnCalc000.setOnClickListener { calcJumlah("000") }

        btnCalc1 = binding.btnCalc1
        btnCalc1.setOnClickListener { calcJumlah("1") }

        btnCalc2 = binding.btnCalc2
        btnCalc2.setOnClickListener { calcJumlah("2") }

        btnCalc3 = binding.btnCalc3
        btnCalc3.setOnClickListener { calcJumlah("3") }

        btnCalc4 = binding.btnCalc4
        btnCalc4.setOnClickListener { calcJumlah("4") }

        btnCalc5 = binding.btnCalc5
        btnCalc5.setOnClickListener { calcJumlah("5") }

        btnCalc6 = binding.btnCalc6
        btnCalc6.setOnClickListener { calcJumlah("6") }

        btnCalc7 = binding.btnCalc7
        btnCalc7.setOnClickListener { calcJumlah("7") }

        btnCalc8 = binding.btnCalc8
        btnCalc8.setOnClickListener { calcJumlah("8") }

        btnCalc9 = binding.btnCalc9
        btnCalc9.setOnClickListener { calcJumlah("9") }

        btnCalcBs = binding.btnCalcBs
        btnCalcBs.setOnClickListener { backSpaceJumlah() }

        return root
    }

    fun backSpaceJumlah() {
        val jumlahString: String = jumlah.toString()

        if (jumlahString.length > 1) {
            jumlah = jumlahString.dropLast(1).toInt()
        }
        else {
            jumlah = 0
        }

        txtJumlah.setText(formatCurrency(jumlah))
    }

    fun calcJumlah(angka: String) {
        val jumlahString: String = jumlah.toString()
        val jumlahNew: String = jumlahString.plus(angka)

        jumlah = jumlahNew.toInt()

        txtJumlah.setText(formatCurrency(jumlah))
    }

    fun formatCurrency(value: Int): String {
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        val formattedValue = currencyFormat.format(value).replace("Rp", "").trim()
        return formattedValue
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}