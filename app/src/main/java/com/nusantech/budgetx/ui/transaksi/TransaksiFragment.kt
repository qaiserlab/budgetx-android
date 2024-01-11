package com.nusantech.budgetx.ui.transaksi

import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nusantech.budgetx.R
import com.nusantech.budgetx.databinding.FragmentTransaksiBinding
import com.nusantech.budgetx.helpers.ApiCall
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class TransaksiFragment : Fragment() {

    private var _binding: FragmentTransaksiBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var apiCall: ApiCall
    lateinit var progressDialog: ProgressDialog

    var jumlah: Int = 0

    lateinit var spnKategori: Spinner
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

    lateinit var btnBuat: Button
    lateinit var btnTetapkanJumlah: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransaksiBinding.inflate(inflater, container, false)
        val root: View = binding.root

        apiCall = ApiCall(requireContext())

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)

        spnKategori = binding.spnKategori

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

        btnBuat = binding.btnBuat
        btnBuat.setOnClickListener { buatKategori()  }

        btnTetapkanJumlah = binding.btnTetapkanJumlah
        btnTetapkanJumlah.setOnClickListener { tetapkanJumlah() }

        fetchKategori()

        return root
    }

    fun tetapkanJumlah() {
        val categoryName = spnKategori.selectedItem.toString()

        if (categoryName.isEmpty()) {
            return Toast.makeText(
                requireContext(),
                "Kategori belum diisi",
                Toast.LENGTH_LONG
            ).show()
        }

        if (jumlah === 0) {
            return Toast.makeText(
                requireContext(),
                "Jumlah belum ditentukan",
                Toast.LENGTH_LONG
            ).show()
        }

        val today: LocalDate = LocalDate.now()
        val tanggal: String = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        apiCall.createTransaction(
            categoryName,
            jumlah,
            tanggal, {items ->
                findNavController().navigate(R.id.navigation_home)
            }, { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            }
        )
    }

    fun buatKategori() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_buat_kategori)

        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT

        dialog.window?.setLayout(width, height)

        val txtNamaKategori: EditText = dialog.findViewById(R.id.txtNamaKategori)
        val radioIncome: RadioButton = dialog.findViewById(R.id.radioIncome)
        val radioExpense: RadioButton = dialog.findViewById(R.id.radioExpense)
        val txtLimit: EditText = dialog.findViewById(R.id.txtLimit)

        val btnBuatKategori: Button = dialog.findViewById(R.id.btnBuatKategori)

        btnBuatKategori.setOnClickListener {
            if (txtNamaKategori.text.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Nama Kategori belum diisi!",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            if (!radioIncome.isChecked && !radioExpense.isChecked) {
                Toast.makeText(
                    requireContext(),
                    "Tipe income/expense harus dipilih salah satu!",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            val categoryName: String = txtNamaKategori.text.toString()
            val type: String = if (radioIncome.isChecked) "Income" else "Expense"
            val limit: Int = if (txtLimit.text.isEmpty()) 0 else txtLimit.text.toString().toInt()

            apiCall.createCategory(
                categoryName,
                type,
                limit, {
                    fetchKategori()
                }, { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                }
            )

            dialog.dismiss()
        }

        dialog.show()
    }

    fun fetchKategori() {
        progressDialog.show()

        apiCall.getCategories({ items ->
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
            spnKategori.adapter = adapter

            progressDialog.dismiss()
        }, { message ->
            progressDialog.dismiss()
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        })
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