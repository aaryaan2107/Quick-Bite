package com.example.menu_order

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import android.view.View
import kotlinx.android.synthetic.main.activity_add_itemand_price.*

class AddItemandPrice : AppCompatActivity() {
    lateinit var DB: SQLiteDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_itemand_price)
//        val spinnerCuisine: Spinner = findViewById(R.id.spinnerCuisine)
//        val spinnerGst: Spinner = findViewById(R.id.spinnergst)
//        val additem: Button = findViewById(R.id.btnadditem)
//        val dishname: EditText = findViewById(R.id.etitemname)
//        val price: EditText = findViewById(R.id.etprice)
//        val gstpriceL:EditText = findViewById(R.id.etpricegst)
//        DB = openOrCreateDatabase("QUICKBITE", MODE_PRIVATE, null)
//
////        var qry = "CREATE TABLE tbl_menuitems (ItemID INTEGER PRIMARY KEY AUTOINCREMENT, Category_Id INTEGER NOT NULL, CategoryName Text NOT NULL,ItemName TEXT NOT NULL, Price INTEGER NOT NULL, PricewithGST INTEGER NOT NULL)"
////        DB.execSQL(qry)
//        // Fetch cuisine details (ID + Name)
//        val cuisineList = mutableListOf<Pair<Int, String>>()
//        val cuisineNames = mutableListOf("Select Cuisine") // For display in spinner
//
//        val cursor: Cursor = DB.rawQuery("SELECT Category_Id, CategoryName FROM tbl_categories", null)
//        while (cursor.moveToNext()) {
//            val categoryId = cursor.getInt(0)
//            val categoryName = cursor.getString(1)
//            cuisineList.add(Pair(categoryId, categoryName))
//            cuisineNames.add(categoryName)
//        }
//        cursor.close()
//
//        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cuisineNames)
//        spinnerCuisine.adapter = adapter
//
//        // Fetch GST rates
//        val gstList = mutableListOf("Select GST Rate")
//        val cursorgst: Cursor = DB.rawQuery("SELECT TaxRate FROM tbl_taxes", null)
//        while (cursorgst.moveToNext()) {
//            gstList.add(cursorgst.getString(0))
//        }
//        cursorgst.close()
//
//        val adaptergst = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, gstList)
//        spinnerGst.adapter = adaptergst
//
//        additem.setOnClickListener {
//            val selectedCuisineIndex = spinnerCuisine.selectedItemPosition
//            val selectedCuisineId = if (selectedCuisineIndex > 0) cuisineList[selectedCuisineIndex - 1].first else null
//            val selectedCuisineName = spinnerCuisine.selectedItem.toString()
//            val selectedGst = spinnerGst.selectedItem.toString()
//
//            if (selectedCuisineId == null || selectedCuisineName == "Select Cuisine" || selectedGst == "Select GST Rate") {
//                Toast.makeText(this, "Please select valid options", Toast.LENGTH_LONG).show()
//                return@setOnClickListener
//            }
//            val dishNameText = dishname.text.toString()
//            val priceText = price.text.toString()
//
//        }
//        val backArrow = findViewById<ImageView>(R.id.back_button)
//        backArrow.setOnClickListener {
//            finish()
//        }

        val spinnerCuisine: Spinner = findViewById(R.id.spinnerCuisine)
        val spinnerGst: Spinner = findViewById(R.id.spinnergst)
        val dishName: EditText = findViewById(R.id.etitemname)
        val price: EditText = findViewById(R.id.etprice)
        val gstPriceL: EditText = findViewById(R.id.etpricegst)
        val addItem: Button = findViewById(R.id.btnadditem)
        //val backArrow = findViewById<ImageView>(R.id.back_button)

        DB = openOrCreateDatabase("QUICKBITE", MODE_PRIVATE, null)

        // Load Cuisine Data
        val cuisineList = mutableListOf<Pair<Int, String>>()
        val cuisineNames = mutableListOf("Select Cuisine")
        val cursor: Cursor = DB.rawQuery("SELECT Category_Id, CategoryName FROM tbl_categories", null)
        while (cursor.moveToNext()) {
            cuisineList.add(Pair(cursor.getInt(0), cursor.getString(1)))
            cuisineNames.add(cursor.getString(1))
        }
        cursor.close()
        spinnerCuisine.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cuisineNames)

        // Load GST Data
        val gstList = mutableListOf("Select GST Rate")
        val cursorGst: Cursor = DB.rawQuery("SELECT TaxRate FROM tbl_taxes", null)
        while (cursorGst.moveToNext()) {
            gstList.add(cursorGst.getString(0))
        }
        cursorGst.close()
        spinnerGst.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, gstList)

        // GST Calculation when GST rate is selected
        spinnerGst.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                gstPriceL.setText(
                    (price.text.toString().toDoubleOrNull()?.let { it + (it * (spinnerGst.selectedItem.toString().toDoubleOrNull() ?: 0.0) / 100) } ?: "").toString()
                )
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Add Item to Database
        addItem.setOnClickListener {
            val selectedCuisineIndex = spinnerCuisine.selectedItemPosition
            val selectedCuisineId = if (selectedCuisineIndex > 0) cuisineList[selectedCuisineIndex - 1].first else null
            val selectedCuisineName = spinnerCuisine.selectedItem.toString()
            val selectedGst = spinnerGst.selectedItem.toString().toDoubleOrNull() ?: 0.0
            val dishNameText = dishName.text.toString().trim()
            val priceValue = price.text.toString().toDoubleOrNull()

            if (selectedCuisineId == null || dishNameText.isEmpty() || priceValue == null || selectedGst == 0.0) {
                Toast.makeText(this, "Please fill all fields correctly!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val priceWithGST = priceValue * (1 + selectedGst / 100)
            DB.execSQL("INSERT INTO tbl_menuitems (Category_Id, CategoryName, ItemName, Price, PricewithGST) VALUES ('$selectedCuisineId', '$selectedCuisineName', '$dishNameText', '$priceValue', '$priceWithGST')")

            etitemname.text.clear()
            etprice.text.clear()
            etpricegst.text.clear()
            spinnerCuisine.setSelection(0)
            spinnerGst.setSelection(0)
            Toast.makeText(this, "Item Added: $dishNameText - $selectedCuisineName ($selectedCuisineId) - GST: $selectedGst%", Toast.LENGTH_LONG).show()
        }
        // Back Button
//        backArrow.setOnClickListener {
//            finish()
//        }
    }
    }
