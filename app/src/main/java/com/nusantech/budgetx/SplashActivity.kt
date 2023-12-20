package com.nusantech.budgetx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.os.HandlerCompat
import com.nusantech.budgetx.helpers.AuthManager

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var handler: Handler = HandlerCompat.createAsync(mainLooper)
        handler.postDelayed(Runnable {
            val authManager = AuthManager(this)
            lateinit var intent: Intent

            if (authManager.isLoggedIn()) {
                intent = Intent(this, MainActivity::class.java)
            }
            else {
                intent = Intent(this, SliderActivity::class.java)
            }

            startActivity(intent)

            finish()
        }, 3000)
    }
}