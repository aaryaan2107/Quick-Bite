package com.example.menu_order

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.widget.*

class AddCategory : AppCompatActivity() {
    lateinit var categoryname : EditText
    lateinit var DB : SQLiteDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        DB = openOrCreateDatabase("quickbite", Context.MODE_PRIVATE,null)

//        var qry = """
//        CREATE TABLE tbl_categories (
//            Category_Id INTEGER PRIMARY KEY AUTOINCREMENT,
//            CategoryName TEXT NOT NULL
//        );
//    """.trimIndent()
//        DB.execSQL(qry)

        val backArrow = findViewById<ImageView>(R.id.back_button)
        backArrow.setOnClickListener {
            val intent = Intent(this,ManagementScreen::class.java)
            startActivity(intent)
        }

        val addcategoriers = findViewById<Button>(R.id.btnSaveCategory)

        addcategoriers.setOnClickListener {
            categoryname = findViewById<EditText>(R.id.etCategoryName)
            var insqry:String
            insqry = "INSERT INTO tbl_categories (CategoryName) VALUES ('${categoryname.text}')"
            DB.execSQL(insqry)
            println(insqry)
        }

    }
}
