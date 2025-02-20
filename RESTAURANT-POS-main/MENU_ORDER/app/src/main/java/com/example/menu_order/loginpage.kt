package com.example.menu_order

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import kotlinx.android.synthetic.main.activity_loginpage.*

class loginpage : AppCompatActivity() {
    lateinit var DB : SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginpage)

        val emailEditText: EditText = findViewById(R.id.username)
        val passwordEditText: EditText = findViewById(R.id.password)

        DB = openOrCreateDatabase("quickbite", Context.MODE_PRIVATE,null)

//        val qry : String = "create table if not exists tbl_user (U_Id INTEGER NOT NULL,username TEXT, password TEXT,PRIMARY KEY(U_Id))"
//        DB.execSQL(qry)
//
//        val insqry :String = "insert into tbl_user values (1,'admin','123456')"
//        DB.execSQL(insqry)


        login.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

          Toast.makeText(this,"$email,$password",Toast.LENGTH_SHORT).show()


            if(email.isNotEmpty() && password.isNotEmpty())
            {
                Toast.makeText(this,"$email,$password",Toast.LENGTH_SHORT).show()
                val cursor :Cursor = DB.rawQuery("select * from tbl_user where username=? AND password=?",
                    arrayOf(email,password))

                if(cursor.count>0)
                {
                    cursor.close()
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeScreen::class.java)
                    startActivity(intent)
                }
                else
                {
                    cursor.close()
                    Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            }


        }
        }


    }

