package com.example.menu_order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class dialog_kot_note : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_kot_note)


        val btnCancel: Button = findViewById(R.id.btnCancel)
        btnCancel.setOnClickListener {
            finish() // Closes the activity
        }
    }
}
