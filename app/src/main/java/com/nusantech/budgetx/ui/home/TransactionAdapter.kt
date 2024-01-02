package com.nusantech.budgetx.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.nusantech.budgetx.R
import java.text.NumberFormat
import java.util.Locale

class TransactionAdapter(context: Context, resource: Int, objects: List<TransactionItem>):
    ArrayAdapter<TransactionItem>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView = convertView?:LayoutInflater.from(context).inflate(R.layout.transaction_item, parent, false)

        val lblCategoryName: TextView = itemView.findViewById(R.id.lblCategoryName)
        val lblTotal: TextView = itemView.findViewById(R.id.lblTotal)
        val lblTanggal: TextView = itemView.findViewById(R.id.lblTanggal)

        val currentItem = getItem(position)

        currentItem?.let {
            lblCategoryName.text = it.category.categoryName

            val currencySymbol: String = if (it.category.type == "Income") "(+)" else "(-)"
            val total = "$currencySymbol ${formatCurrency(it.total)}"

            lblTotal.text = total
            lblTanggal.text = it.tanggal
        }
        return itemView
    }

    fun formatCurrency(value: Int): String {
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        val formattedValue = currencyFormat.format(value).replace("Rp", "").trim()
        return formattedValue
    }


}