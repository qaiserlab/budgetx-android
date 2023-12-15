package com.nusantech.budgetx

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.nusantech.budgetx.helpers.AuthManager

class RegisterActivity : AppCompatActivity() {
    lateinit var txtNewUsername: EditText
    lateinit var txtEmail: EditText
    lateinit var txtNewPassword: EditText

    lateinit var btnRegister: Button
    lateinit var linkLogin: TextView

    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        txtNewUsername = findViewById(R.id.txtNewUsername)
        txtEmail = findViewById(R.id.txtEmail)
        txtNewPassword = findViewById(R.id.txtNewPassword)

        btnRegister = findViewById(R.id.btnRegister)
        btnRegister.setOnClickListener { btnRegister_onClick() }

        linkLogin = findViewById(R.id.linkLogin)
        linkLogin.setOnClickListener { linkLogin_onClick() }

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)
    }

    fun btnRegister_onClick() {
        val authManager = AuthManager(this)
        progressDialog.show()

        authManager.register(
            txtNewUsername.text.toString(),
            txtEmail.text.toString(),
            txtNewPassword.text.toString(),
            { message ->
                progressDialog.dismiss()
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                finish()
            }, { message ->
                progressDialog.dismiss()
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        )

    }

    fun linkLogin_onClick() {
        finish()
    }
}