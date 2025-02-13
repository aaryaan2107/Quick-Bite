package com.example.menu_order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import android.content.Intent
import kotlinx.android.synthetic.main.activity_loginpage.*

class loginpage : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginpage)

        val emailEditText: EditText = findViewById(R.id.username)
        val passwordEditText: EditText = findViewById(R.id.password)


        login.setOnClickListener {
//            emailEditText.setText("admin")
//            passwordEditText.setText("admin")
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email == "admin" && password == "admin") {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, HomeScreen::class.java)
                startActivity(intent)

            } else {
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
            }
        }


    }
}
