package com.example.menu_order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class modifier : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modifier)


        val backmodifier:ImageView = findViewById(R.id.back_modifier)
        backmodifier.setOnClickListener {
            finish() // Go back to the previous activity
        }
        // Add Modifier button click event
        val addmodifier: ImageView = findViewById(R.id.add_modifier)
        addmodifier.setOnClickListener {
            Toast.makeText(this, "Add Modifier Clicked" , Toast.LENGTH_SHORT).show()
        }


    }
    }

