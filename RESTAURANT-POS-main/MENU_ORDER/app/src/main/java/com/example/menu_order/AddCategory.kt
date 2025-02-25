//package com.example.menu_order
//
//import android.content.Context
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.content.Intent
//import android.database.Cursor
//import android.database.sqlite.SQLiteDatabase
//import android.widget.*
//import kotlinx.android.synthetic.main.activity_add_category.*
//
//class AddCategory : AppCompatActivity() {
//    lateinit var categoryname : EditText
//    lateinit var lvcat : ListView
//    lateinit var DB : SQLiteDatabase
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_add_category)
//
//        DB = openOrCreateDatabase("QUICKBITE", Context.MODE_PRIVATE,null)
//
////        var qry = """
////        CREATE TABLE tbl_categories (
////            Category_Id INTEGER PRIMARY KEY AUTOINCREMENT,
////            CategoryName TEXT NOT NULL
////        );
////    """.trimIndent()
////        DB.execSQL(qry)
//
//
//        val addcategoriers = findViewById<Button>(R.id.btnSaveCategory)
//
//        addcategoriers.setOnClickListener {
//            categoryname = findViewById<EditText>(R.id.etCategoryName)
//            var insqry:String
//            insqry = "INSERT INTO tbl_categories (CategoryName) VALUES ('${categoryname.text}')"
//            DB.execSQL(insqry)
//            println(insqry)
//            etCategoryName.text = null
//        }
//
//
//    }
//}

package com.example.menu_order

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AddCategory : AppCompatActivity() {
    lateinit var categoryName: EditText
    lateinit var lvCategory: ListView
    lateinit var db: SQLiteDatabase
    lateinit var adapter: ArrayAdapter<String>
    val categoryList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        // Initialize views
        categoryName = findViewById(R.id.etCategoryName)
        lvCategory = findViewById(R.id.lvcategory)
        val btnSaveCategory = findViewById<Button>(R.id.btnSaveCategory)

        // Open database
        db = openOrCreateDatabase("QUICKBITE", Context.MODE_PRIVATE, null)

        // Create table if not exists
//        val createTableQuery = """
//            CREATE TABLE IF NOT EXISTS tbl_categories (
//                Category_Id INTEGER PRIMARY KEY AUTOINCREMENT,
//                CategoryName TEXT NOT NULL
//            );
//        """.trimIndent()
//        db.execSQL(createTableQuery)

        // Load categories into ListView (directly inside onCreate)
        categoryList.clear() // Clear existing list
        val cursor: Cursor = db.rawQuery("SELECT CategoryName FROM tbl_categories", null)
        if (cursor.moveToFirst()) {
            do {
                categoryList.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }
        cursor.close()

        // Set up ListView adapter
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, categoryList)
        lvCategory.adapter = adapter

        // Add category button click
        btnSaveCategory.setOnClickListener {
            val categoryText = categoryName.text.toString().trim()

            if (categoryText.isNotEmpty()) {
                val insertQuery = "INSERT INTO tbl_categories (CategoryName) VALUES ('$categoryText')"
                db.execSQL(insertQuery)

                // Clear input field
                categoryName.text.clear()

                // Refresh list directly inside onCreate
                categoryList.clear()
                val cursorUpdate: Cursor = db.rawQuery("SELECT CategoryName FROM tbl_categories", null)
                if (cursorUpdate.moveToFirst()) {
                    do {
                        categoryList.add(cursorUpdate.getString(0))
                    } while (cursorUpdate.moveToNext())
                }
                cursorUpdate.close()

                adapter.notifyDataSetChanged() // Update ListView
            } else {
                Toast.makeText(this, "Enter a category name", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
