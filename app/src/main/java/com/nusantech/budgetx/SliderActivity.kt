package com.nusantech.budgetx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2
import com.nusantech.budgetx.ui.sliders.SliderAdapter

class SliderActivity : AppCompatActivity() {
    lateinit var btnSkip: Button
    lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slider)

        btnSkip = findViewById(R.id.btnSkip)

        btnSkip.setOnClickListener { btnSkip_onClick() }

        viewPager = findViewById(R.id.viewPager)
        val adapter = SliderAdapter(this)

        viewPager.adapter = adapter
    }

    fun btnSkip_onClick() {
        val intent: Intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

        finish()
    }
}