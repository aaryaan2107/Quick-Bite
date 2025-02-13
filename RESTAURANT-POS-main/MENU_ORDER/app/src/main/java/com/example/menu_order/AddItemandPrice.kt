package com.example.menu_order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import android.view.View

class AddItemandPrice : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_itemand_price)
        val spinnerCuisine: Spinner = findViewById(R.id.spinnerCuisine)

        // Define Cuisine List
        val cuisineList = listOf("Select Food", "Gujarati", "Chinese", "Punjabi", "South Indian", "Breads", "Beverages","Desserts")
        // Create Adapter for Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cuisineList)
        spinnerCuisine.adapter = adapter

        // Handle Spinner Item Selection
        spinnerCuisine.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedCuisine = cuisineList[position]
                if (position != 0) { // Avoid showing "Select Cuisine"
                    Toast.makeText(this@AddItemandPrice, "Selected: $selectedCuisine", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }
    }
