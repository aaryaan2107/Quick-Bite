package com.example.menu_order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class manage_list : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_list)

        val rebillingButton = findViewById<Button>(R.id.btn_rebilling_reason)
        val cancelOrderButton = findViewById<Button>(R.id.btn_cancel_order_reason)
        val cancelItemButton = findViewById<Button>(R.id.btn_cancel_item_reason)
        val optionsButton = findViewById<Button>(R.id.btn_options)
        val ncPaymentButton = findViewById<Button>(R.id.btn_nc_payment_type)
        val payoutButton = findViewById<Button>(R.id.btn_payout)
        val unitTypeButton = findViewById<Button>(R.id.btn_unit_type)

        // Button Click Events
        rebillingButton.setOnClickListener {
            Toast.makeText(this, "Re-billing Reason Clicked", Toast.LENGTH_SHORT).show()
        }

        cancelOrderButton.setOnClickListener {
            Toast.makeText(this, "Cancel Order Reason Clicked", Toast.LENGTH_SHORT).show()
        }

        cancelItemButton.setOnClickListener {
            Toast.makeText(this, "Cancel Item Reason Clicked", Toast.LENGTH_SHORT).show()
        }

        optionsButton.setOnClickListener {
            Toast.makeText(this, "Options Clicked", Toast.LENGTH_SHORT).show()
        }

        ncPaymentButton.setOnClickListener {
            Toast.makeText(this, "NC Payment Type Clicked", Toast.LENGTH_SHORT).show()
        }

        payoutButton.setOnClickListener {
            Toast.makeText(this, "Payout Clicked", Toast.LENGTH_SHORT).show()
        }

        unitTypeButton.setOnClickListener {
            Toast.makeText(this, "Unit Type Clicked", Toast.LENGTH_SHORT).show()
        }
    }
}
