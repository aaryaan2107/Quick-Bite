package com.example.menu_order

import android.app.Dialog
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var spinnerCuisine: Spinner
    private lateinit var listViewDishes: ListView
    private lateinit var etQuantity: EditText
    private lateinit var btnAddToOrder: Button
    private lateinit var btnConfirmOrder: Button
    private lateinit var btnOrderedFood: Button
    private lateinit var btnnotes: Button
    private lateinit var tvOrderSummary: TextView

    private val selectedOrders = mutableListOf<Triple<String, Int, Int>>()
    private var selectedItem: String? = null
    private var selectedPrice: Int? = 0
    private lateinit var DB: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinnerCuisine = findViewById(R.id.spinnerCuisine)
        listViewDishes = findViewById(R.id.listViewDishes)
        etQuantity = findViewById(R.id.etQuantity)
        btnAddToOrder = findViewById(R.id.btnAddToOrder)
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder)
        btnOrderedFood = findViewById(R.id.btnorderedfood)
        btnnotes = findViewById(R.id.addnotes)
        tvOrderSummary = findViewById(R.id.tvOrderSummary) // Display added order items


        btnnotes.setOnClickListener {
            val intent = Intent(this, dialog_kot_note::class.java)
            startActivity(intent) // Opens the note screen
        }
        var actionBar = getSupportActionBar()
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        DB = openOrCreateDatabase("QUICKBITE", MODE_PRIVATE, null)


//        DB.execSQL("CREATE TABLE IF NOT EXISTS tbl_orderdetails (OrderDetailID INTEGER PRIMARY KEY AUTOINCREMENT, TableID INTEGER, OrderID INTEGER, ItemID INTEGER, ItemName TEXT, Quantity INTEGER NOT NULL, Price INTEGER NOT NULL, Total INTEGER NOT NULL)")
//        DB.execSQL("CREATE TABLE IF NOT EXISTS tbl_orders (OrderID INTEGER PRIMARY KEY AUTOINCREMENT, TableID INTEGER, OrderDateTime TEXT NOT NULL, TotalAmount INTEGER)")
//
//        DB.execSQL(
//            "CREATE TABLE IF NOT EXISTS tbl_orders (" +
//                    "OrderID INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                    "TableID INTEGER, " +
//                    "SessionID INTEGER, " +  // NEW: Grouping orders for a table
//                    "OrderDateTime TEXT NOT NULL, " +
//                    "TotalAmount INTEGER)"
//        )
//
//        DB.execSQL(
//            "CREATE TABLE IF NOT EXISTS tbl_orderdetails (" +
//                    "OrderDetailID INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                    "TableID INTEGER, " +
//                    "SessionID INTEGER, " +  // NEW: Grouping order items
//                    "OrderID INTEGER, " +
//                    "ItemID INTEGER, " +
//                    "ItemName TEXT, " +
//                    "Quantity INTEGER NOT NULL, " +
//                    "Price INTEGER NOT NULL, " +
//                    "Total INTEGER NOT NULL)"
//        )



        val tableId = intent.getIntExtra("TABLE_ID", -1)
        if (tableId != -1) {
            Toast.makeText(this, "Selected Table: $tableId", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No table selected!", Toast.LENGTH_SHORT).show()
        }
        val cuisineList = mutableListOf<Pair<Int, String>>()
        val cuisineNames = mutableListOf("Select Cuisine")
        val cursor: Cursor = DB.rawQuery("SELECT Category_Id, CategoryName FROM tbl_categories", null)
        while (cursor.moveToNext()) {
            cuisineList.add(Pair(cursor.getInt(0), cursor.getString(1)))
            cuisineNames.add(cursor.getString(1))
        }
        cursor.close()
        spinnerCuisine.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cuisineNames)

        spinnerCuisine.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position > 0) {
                    val selectedCategoryId = cuisineList[position - 1].first
                    val menuList = mutableListOf<String>()
                    val itemDetails = mutableListOf<Pair<String, Int>>()

                    val menuCursor: Cursor = DB.rawQuery(
                        "SELECT ItemName, PricewithGST FROM tbl_menuitems WHERE Category_Id = ?",
                        arrayOf(selectedCategoryId.toString())
                    )

                    while (menuCursor.moveToNext()) {
                        val itemName = menuCursor.getString(0)
                        val priceWithGST = menuCursor.getInt(1)
                        menuList.add("$itemName - ₹$priceWithGST")
                        itemDetails.add(Pair(itemName, priceWithGST))
                    }
                    menuCursor.close()

                    val menuAdapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, menuList)
                    listViewDishes.adapter = menuAdapter

                    listViewDishes.setOnItemClickListener { _, _, pos, _ ->
                        selectedItem = itemDetails[pos].first
                        selectedPrice = itemDetails[pos].second
                        Toast.makeText(this@MainActivity, "Selected: $selectedItem - ₹$selectedPrice", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    listViewDishes.adapter = null
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnAddToOrder.setOnClickListener {
            if (selectedItem == null) {
                Toast.makeText(this, "Select a food item first!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val quantityText = etQuantity.text.toString()
            if (quantityText.isEmpty()) {
                Toast.makeText(this, "Enter quantity for $selectedItem!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val quantity = quantityText.toInt()
            selectedOrders.add(Triple(selectedItem!!, selectedPrice!!, quantity))
            etQuantity.text = null
            updateOrderSummary()
        }

        btnOrderedFood.setOnClickListener {
            if (selectedOrders.isEmpty()) {
                Toast.makeText(this, "No order to place!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val orderDateTime: String = dateFormat.format(Date())
            var totalAmount = 0
            for (order in selectedOrders) {
                totalAmount += order.second * order.third
            }

            // 🔹 Fetch the latest SessionID for this TableID
            val sessionCursor = DB.rawQuery(
                "SELECT MAX(SessionID) FROM tbl_orders WHERE TableID = ?",
                arrayOf(tableId.toString())
            )

            var sessionId = 0
            if (sessionCursor.moveToFirst()) {
                sessionId = sessionCursor.getInt(0) // Get the latest session
            }
            sessionCursor.close()

            // 🔹 If no active session, create a new one
            if (sessionId == 0) {
                sessionId = System.currentTimeMillis().toInt() // Unique session ID
            }

            // 🔹 Insert the Order with SessionID
            DB.execSQL(
                "INSERT INTO tbl_orders (TableID, SessionID, OrderDateTime, TotalAmount) VALUES ($tableId, $sessionId, '$orderDateTime', $totalAmount)"
            )

            val orderCursor = DB.rawQuery("SELECT last_insert_rowid()", null)
            orderCursor.moveToFirst()
            val orderId = orderCursor.getInt(0)
            orderCursor.close()

            // 🔹 Insert Order Details with SessionID
            for ((itemName, price, qty) in selectedOrders) {
                val itemCursor = DB.rawQuery(
                    "SELECT ItemID FROM tbl_menuitems WHERE ItemName = ?",
                    arrayOf(itemName)
                )

                if (itemCursor.moveToFirst()) {
                    val itemId = itemCursor.getInt(0)
                    DB.execSQL(
                        "INSERT INTO tbl_orderdetails (TableID, SessionID, OrderID, ItemID, ItemName, Quantity, Price, Total) " +
                                "VALUES ($tableId, $sessionId, $orderId, $itemId, '$itemName', $qty, $price, ${price * qty})"
                    )
                }
                itemCursor.close()
            }

            selectedOrders.clear()
            tvOrderSummary.text = "Order Placed Successfully!"
            Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show()
        }

        btnConfirmOrder.setOnClickListener {
            val intent = Intent(this, Bill::class.java) // Change to the correct target Activity
            intent.putExtra("TABLE_ID", tableId) // Pass the table number
            startActivity(intent)
        }
    }

 private fun updateOrderSummary() {
        var orderSummary = "Current Order:\n"
        var totalAmount = 0

        for ((item, price, qty) in selectedOrders) {
            val total = price * qty
            totalAmount += total
            orderSummary += "$item x$qty = ₹$total\n"
        }

        orderSummary += "\nTotal: ₹$totalAmount"
        tvOrderSummary.text = orderSummary
    }

override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return true
}

}
