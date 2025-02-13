package com.example.menu_order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.content.Intent

class AddCategory : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        val backArrow = findViewById<ImageView>(R.id.back_button)
        backArrow.setOnClickListener {
//            onBackPressed() // Ensure you are using the correct syntax
            val intent = Intent(this,ManagementScreen::class.java)
            startActivity(intent)
        }
    }
}
