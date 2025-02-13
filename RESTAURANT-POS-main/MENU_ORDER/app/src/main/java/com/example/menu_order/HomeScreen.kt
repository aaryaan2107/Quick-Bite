package com.example.menu_order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.media.Image
import android.widget.ImageButton
import android.widget.Toast
import android.widget.ImageView

class HomeScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        val dineInBtn = findViewById<ImageButton>(R.id.btn_dine_in)
        val manageBtn = findViewById<ImageButton>(R.id.btn_manage)
        val quickBtn = findViewById<ImageButton>(R.id.btn_quick)
        val reportBtn = findViewById<ImageButton>(R.id.btn_report)
        val helpBtn = findViewById<ImageButton>(R.id.btn_help)



        dineInBtn.setOnClickListener {
            val intent = Intent(this,Tables_layout::class.java)
            startActivity(intent)
        }
        manageBtn.setOnClickListener {
            val intent  = Intent(this,ManagementScreen::class.java)
            startActivity(intent)
        }
        quickBtn.setOnClickListener {
            val intent  = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        helpBtn.setOnClickListener {
//            val intent  = Intent(this,MainActivity::class.java)
//            startActivity(intent)
            Toast.makeText(this,"Help Help Help",Toast.LENGTH_SHORT).show()
        }
    }
}
