package com.nusantech.budgetx.helpers

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
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
}