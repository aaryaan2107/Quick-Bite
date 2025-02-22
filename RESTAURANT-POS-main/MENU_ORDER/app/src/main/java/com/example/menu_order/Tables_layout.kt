package com.example.menu_order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Button

class Tables_layout : AppCompatActivity() {

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed() // This will navigate to the previous screen
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tables_layout)
        
//        val backArrow = findViewById<ImageView>(R.id.back_button)
//        backArrow.setOnClickListener {
////            onBackPressed() // Ensure you are using the correct syntax
//            val intent = Intent(this,HomeScreen::class.java)
//            startActivity(intent)
//        }

//        val tableIds = listOf(
//            R.id.table1, R.id.table2, R.id.table3, R.id.table4, R.id.table5,
//            R.id.table6, R.id.table7, R.id.table8, R.id.table9, R.id.table10
//        )
//
//
//
//
//
//        // Loop through each table ID and set click listener
//        for (id in tableIds) {
//            findViewById<View>(id)?.setOnClickListener {
//                val intent = Intent(this, MainActivity::class.java) // Change to the correct target Activity
//                intent.putExtra("TABLE_ID", selectedTableId) // Pass the table ID
//                startActivity(intent)
//            }
//        }
        val tableIds = listOf(
            R.id.table1, R.id.table2, R.id.table3, R.id.table4, R.id.table5,
            R.id.table6, R.id.table7, R.id.table8, R.id.table9, R.id.table10
        )

// Loop through each table ID and set click listener
        for (id in tableIds) {
            findViewById<View>(id)?.setOnClickListener {
                val selectedTableId = tableIds.indexOf(id) + 1 // Get table number (1 to 10)

                val intent = Intent(this, MainActivity::class.java) // Change to the correct target Activity
                intent.putExtra("TABLE_ID", selectedTableId) // Pass the table number
                startActivity(intent)
            }
        }
    }
}
