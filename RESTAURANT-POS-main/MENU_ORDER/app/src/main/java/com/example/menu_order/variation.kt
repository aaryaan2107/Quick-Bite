package com.example.menu_order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
class variation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_variation)
        val backButton:ImageView = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            finish() // Go back to the previous activity
        }
        // Add Variation button click event
        val addvariation:ImageView = findViewById(R.id.add_variation)
        addvariation.setOnClickListener {
            Toast.makeText(this, "Add Variation Clicked" , Toast.LENGTH_SHORT).show()
        }
//        val backArrow = findViewById<ImageView>(R.id.back_button)
//        backArrow.setOnClickListener {
////            onBackPressed() // Ensure you are using the correct syntax
//            val intent = Intent(this,ManagementScreen::class.java)
//            startActivity(intent)
//        }
    }
}
