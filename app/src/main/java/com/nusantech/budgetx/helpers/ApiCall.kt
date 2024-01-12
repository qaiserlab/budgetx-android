package com.nusantech.budgetx.helpers

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.nusantech.budgetx.ui.home.CategoryRecord
import com.nusantech.budgetx.ui.home.TransactionItem
import org.json.JSONException

class ApiCall(_context: Context) {
    val baseUrl = "https://my-json-server.typicode.com/qaiserlab/budgetx-android"
    val context: Context = _context

    fun getReport(
        onSuccess: (expenseTotal: Int, expensePercentage: Int, incomeTotal: Int, incomePercentage: Int) -> Unit,
        onError: (message: String) -> Unit
    ) {
        val url = "$baseUrl/reports"

        val requestQueue = Volley.newRequestQueue(context)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val expense = response.getJSONObject("expense")
                    val income = response.getJSONObject("income")

                    val expenseTotal = expense.getInt("total")
                    val expensePercentage = expense.getInt("percentage")

                    val incomeTotal = income.getInt("total")
                    val incomePercentage = income.getInt("percentage")

                    onSuccess(expenseTotal, expensePercentage, incomeTotal, incomePercentage)
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

    fun createTransaction(
        categoryName: String,
        jumlah: Int,
        tanggal: String,
        onSuccess: (message: String) -> Unit,
        onError: (message: String) -> Unit
    ) {
        val url = "$baseUrl/transactions"
        val requestQueue = Volley.newRequestQueue(context)

        val request = object : StringRequest(
            Method.POST,
            url,
            Response.Listener { response ->
                onSuccess.invoke("Berhasil membuat transaksi")
            },
            Response.ErrorListener { error ->
                onError.invoke("Gagal membuat transaksi")
            }
        ) {
            override fun getParams(): Map<String, String>? {
                val params = HashMap<String, String>()
                params["categoryName"] = categoryName
                params["jumlah"] = jumlah.toString()
                params["tanggal"] = tanggal
                return params
            }
        }

        requestQueue.add(request)
    }
    fun getCategories(
        onSuccess: (MutableList<String>) -> Unit,
        onError: (message: String) -> Unit
    ) {
        val url = "$baseUrl/categories"

        val requestQueue = Volley.newRequestQueue(context)

        val jsonObjectRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                try {
                    val items = mutableListOf<String>("")

                    for (i in 0 until response.length()) {
                        val categoryObject = response.getJSONObject(i)
                        val categoryName = categoryObject.getString("categoryName")

                        items.add(categoryName)
                    }

                    onSuccess.invoke(items)
                }
                catch (e: JSONException) {
                    onError.invoke("Gagal mendapatkan data")
                }
            }, { error ->
                onError.invoke("Gagal mendapatkan data")
            }
        )

        requestQueue.add(jsonObjectRequest)
    }

    fun createCategory(
        categoryName: String,
        type: String,
        limit: Int,
        onSuccess: (message: String) -> Unit,
        onError: (message: String) -> Unit
    ) {
        val url = "$baseUrl/categories"
        val requestQueue = Volley.newRequestQueue(context)

        val request = object : StringRequest(
            Method.POST,
            url,
            Response.Listener { response ->
                onSuccess.invoke("Berhasil membuat kategori")
            },
            Response.ErrorListener {
                onError.invoke("Gagal membuat kategori")
            }
        ) {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["categoryName"] = categoryName
                params["type"] = type
                params["limit"] = limit.toString()
                return params
            }
        }

        requestQueue.add(request)
    }
}