package com.example.menu_order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class Tables_layout : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tables_layout)

//        val tb1 : Button = findViewById(R.id.table1)
//
//        tb1.setOnClickListener {
//            val intent = Intent(this,MainActivity::class.java)
//            startActivity(intent)
//
//        }


        val tableIds = listOf(
            R.id.table1, R.id.table2, R.id.table3, R.id.table4, R.id.table5,
            R.id.table6, R.id.table7, R.id.table8, R.id.table9, R.id.table10
        )

        // Loop through each table ID and set click listener
        for (id in tableIds) {
            findViewById<View>(id)?.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java) // Change to the correct target Activity
                startActivity(intent)
            }
        }
    }
}
