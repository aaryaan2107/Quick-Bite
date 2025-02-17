package com.example.menu_order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import kotlinx.android.synthetic.main.activity_management_screen.*

class ManagementScreen : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_management_screen)

        val payment : ImageView = findViewById(R.id.payments)
        val category : ImageView = findViewById(R.id.Category)
        val item : ImageView = findViewById(R.id.items)
        //val manage: ImageView = findViewById(R.id.managelist)
        val modify:ImageView = findViewById(R.id.Modifier)
        val variations : ImageView = findViewById(R.id.Variation)
        val backArrow = findViewById<ImageView>(R.id.back_button)
        backArrow.setOnClickListener {
            finish()
        }
        payment.setOnClickListener {
            val intent = Intent(this,AddGST::class.java)
            startActivity(intent)
        }
        category.setOnClickListener {
           val intent = Intent(this,AddCategory::class.java)
            startActivity(intent)
        }
        item.setOnClickListener {
            val intent = Intent(this,AddItemandPrice::class.java)
            startActivity(intent)
        }
//        manage.setOnClickListener {
//            val intent = Intent(this,manage_list::class.java)
//            startActivity(intent)
//        }
        modify.setOnClickListener {
            val intent=Intent(this,modifier::class.java)
            startActivity(intent)
        }
        variations.setOnClickListener {
            val intent=Intent(this,variation::class.java)
            startActivity(intent)
        }
    }

}
