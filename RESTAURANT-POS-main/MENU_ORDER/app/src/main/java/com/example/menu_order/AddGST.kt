package com.example.menu_order

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView

class AddGST : AppCompatActivity() {


    lateinit var DB : SQLiteDatabase
    lateinit var gstpercentage : EditText
    lateinit var gstdetails : EditText
    lateinit var addgst : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_gst)

        DB = openOrCreateDatabase("QUICKBITE", Context.MODE_PRIVATE,null)

//        var qry = "CREATE TABLE tbl_taxes (TaxID INTEGER PRIMARY KEY AUTOINCREMENT,TaxName TEXT NOT NULL,TaxRate INTEGER NOT NULL)"
//        DB.execSQL(qry)


        addgst = findViewById(R.id.btnSaveGst)
        gstpercentage = findViewById(R.id.etGstPercentage)
        gstdetails = findViewById(R.id.etGstDescription)
        addgst.setOnClickListener {
            var insqry = "INSERT INTO tbl_taxes (TaxName,TaxRate) VALUES ('${gstdetails.text}','${gstpercentage.text}')"
            DB.execSQL(insqry)
        }

//        val backArrow = findViewById<ImageView>(R.id.back_button)
//        backArrow.setOnClickListener {
//        finish()
//        }
    }
}
