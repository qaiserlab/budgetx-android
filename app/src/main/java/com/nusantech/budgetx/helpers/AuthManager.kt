package com.nusantech.budgetx.helpers

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.nusantech.budgetx.MainActivity
import org.json.JSONArray
import org.json.JSONObject

class AuthManager(private val context: Context) {
    private val baseUrl = "https://lemur-8.cloud-iam.com"
    private val clientId = "be-sso-client"
    private val clientSecret = "Ggv9XsZhsNeqp0niZHIHgcyyDWaZL5J8"

    private val adminUsername = "admin"
    private val adminPassword = "P:5kQOypakBB3]0sPc,c"
    private var adminAccessToken: String = ""

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    fun login(
        username: String,
        password: String,
        onSuccess: (accessToken: String) -> Unit,
        onError: (message: String) -> Unit
    ) {
        val loginUrl = "$baseUrl/auth/realms/dev-keycloak/protocol/openid-connect/token"

        val request = object : StringRequest(
            Method.POST, loginUrl,
            Response.Listener { response ->
                val jsonResponse = JSONObject(response)
                val accessToken = jsonResponse.getString("access_token")

                onSuccess.invoke(accessToken)
            },
            Response.ErrorListener { error ->
                onError.invoke("Login gagal!")
            }
        ) {

            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["grant_type"] = "password"
                params["client_id"] = clientId
                params["client_secret"] = clientSecret
                params["username"] = username
                params["password"] = password
                params["scope"] = "openid"
                return params
            }

        }

        requestQueue.add(request)
    }

    fun register(
        username: String,
        email: String,
        password: String,
        onSuccess: (message: String) -> Unit,
        onError: (message: String) -> Unit
    ) {
        val registerUrl = "$baseUrl/auth/admin/realms/dev-keycloak/users"

        val registerData = JSONObject().apply {
            put("username", username)
            put("email", email)
            put("enabled", true)
            put("emailVerified", true)

            val credentials = JSONArray()
            val credential = JSONObject().apply {
                put("temporary", false)
                put("type", "password")
                put("value", password)
            }
            credentials.put(credential)

            put("credentials", credentials)
        }

        val request = object: JsonObjectRequest(
            Method.POST, registerUrl, registerData,
            Response.Listener { response ->
                onSuccess.invoke("User berhasil diregistrasi!")
            },
            Response.ErrorListener { error ->
                val statusCode = error.networkResponse.statusCode

                if (statusCode === 201) {
                    onSuccess.invoke("User berhasil diregistrasi!")
                }
                else {
                    onError.invoke("Registrasi gagal!")
                }
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Authorization"] = "Bearer $adminAccessToken"
                return headers
            }
        }

        if (adminAccessToken !== "") {
            requestQueue.add(request)
        }
        else {
            login(adminUsername, adminPassword, {accessToken ->
                Log.i("access token", accessToken)
                adminAccessToken = accessToken
                requestQueue.add(request)
            }, {
                onError.invoke("Registrasi gagal!")
            })
        }

    }
}