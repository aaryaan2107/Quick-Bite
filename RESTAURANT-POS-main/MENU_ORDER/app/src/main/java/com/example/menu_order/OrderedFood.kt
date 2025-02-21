package com.example.menu_order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import  android.widget.*

class OrderedFood : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ordered_food)
//        val imgBack: ImageView = findViewById(R.id.imgback)
//
//        imgBack.setOnClickListener {
//            finish() // Close this activity and return to the previous one
//        }
        val tvOrderedItems: TextView = findViewById(R.id.tvOrderedItems)
        val tvTotalAmount: TextView = findViewById(R.id.tvTotalAmount)

        val orderList = intent.getStringArrayListExtra("orderList")
        val totalAmount = intent.getIntExtra("totalAmount", 0)

        tvOrderedItems.text = orderList?.joinToString("\n") ?: "No items ordered"
        tvTotalAmount.text = "Total: ₹$totalAmount"
    }
}
