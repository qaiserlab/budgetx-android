package com.nusantech.budgetx.helpers

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.nusantech.budgetx.ui.home.CategoryRecord
import com.nusantech.budgetx.ui.home.TransactionItem
import org.json.JSONException

class ApiCall(_context: Context) {
    val baseUrl = "https://my-json-server.typicode.com/qaiserlab/budgetx-android"
    val context: Context = _context

    fun getMonthlyOverview(
        onSuccess: (type: String, total: Int) -> Unit,
        onError: (message: String) -> Unit
    ) {
        val url = "$baseUrl/monthly-overview"

        val requestQueue = Volley.newRequestQueue(context)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val type = response.getString("type")
                    val total = response.getInt("total")

                    onSuccess(type, total)
                }
                catch (e: JSONException) {
                    onError("Gagal mendapatkan data")
                }
            },
            { error ->
                onError("Gagal mendapatkan data")
            }
        )

        requestQueue.add(jsonObjectRequest)
    }

    fun getTransactions(
        onSuccess: (MutableList<TransactionItem>) -> Unit,
        onError: (message: String) -> Unit
    ) {
        val url = "$baseUrl/transactions"

        val requestQueue = Volley.newRequestQueue(context)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val transactionArray = response.getJSONArray("monthly")
                    val items = mutableListOf<TransactionItem>()

                    for (i in 0 until transactionArray.length()) {
                        val transactionObject = transactionArray.getJSONObject(i)

                        val id = transactionObject.getInt("id")
                        val category = transactionObject.getJSONObject("category")
                        val total = transactionObject.getInt("total")
                        val tanggal = transactionObject.getString("transactionDate")

                        val categoryName = category.getString("categoryName")
                        val type = category.getString("type")
                        val limit = category.getInt("limit")

                        val categoryRecord: CategoryRecord = CategoryRecord(
                            id,
                            categoryName,
                            type,
                            limit
                        )

                        val listItem = TransactionItem(id, categoryRecord, total, tanggal)

                        items.add(listItem)
                    }

                    onSuccess(items)
                }
                catch (e: JSONException) {
                    onError("Gagal mendapatkan data")
                }
            },
            { error ->
                onError("Gagal mendapatkan data")
            }
        )

        requestQueue.add(jsonObjectRequest)
    }
}