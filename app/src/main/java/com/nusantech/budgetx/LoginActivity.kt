package com.nusantech.budgetx

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.toolbox.Volley
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.nusantech.budgetx.helpers.AuthManager
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    lateinit var txtUsername: EditText
    lateinit var txtPassword: EditText
    lateinit var btnLogin: Button
    lateinit var linkRegister: TextView

    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        txtUsername = findViewById(R.id.txtUsername)
        txtPassword = findViewById(R.id.txtPassword)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener { btnLogin_onClick() }

        linkRegister = findViewById(R.id.linkRegister)

        linkRegister.setOnClickListener { linkRegister_onClick() }

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)
    }

    fun btnLogin_onClick() {
        val authManager = AuthManager(this)
        progressDialog.show()

        authManager.login(
            txtUsername.text.toString(),
            txtPassword.text.toString(), { accessToken ->
                progressDialog.dismiss()

                val intent: Intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                finish()
            }, { message ->
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                progressDialog.dismiss()
            }
        )

    }

    fun linkRegister_onClick() {
        val intent: Intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}