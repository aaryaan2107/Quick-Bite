package com.example.menu_order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class AddGST : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_gst)

        val backArrow = findViewById<ImageView>(R.id.back_button)
        backArrow.setOnClickListener {
//            onBackPressed() // Ensure you are using the correct syntax
        finish()
        }
    }
}
