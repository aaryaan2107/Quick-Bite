package com.example.menu_order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import android.app.Dialog
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var spinnerCuisine: Spinner
    private lateinit var listViewDishes: ListView
    private lateinit var etQuantity: EditText
    private lateinit var btnAddToOrder: Button
    private lateinit var tvOrderSummary: TextView
    private lateinit var btnConfirmOrder: Button
    private lateinit var btnOrderedFood: Button
    private lateinit var btnnotes : Button

    private val cuisineMap = hashMapOf(
        "South Indian" to mapOf(
            "Dosa" to 80,
            "Idli Sambar" to 60,
            "Uttapam" to 90,
            "Vada" to 50,
            "Rasam Rice" to 70,
            "Pongal" to 100,
            "Appam" to 120,
            "Pesarattu" to 110
        ),
        "Punjabi" to mapOf(
            "Paneer Butter Masala" to 180,
            "Chole Bhature" to 130,
            "Rajma Chawal" to 120,
            "Sarson da Saag" to 150,
            "Dal Makhani" to 130,
            "Butter Chicken" to 200,
            "Amritsari Kulcha" to 160
        ),
        "Gujarati" to mapOf(
            "Dhokla" to 60,
            "Thepla" to 70,
            "Khandvi" to 80,
            "Undhiyu" to 150,
            "Sev Tameta" to 100,
            "Fafda" to 40,
            "Handvo" to 120,
            "Gujarati Kadhi" to 80
        ),
        "Beverages" to mapOf(
            "Masala Chai" to 40,
            "Lassi" to 50,
            "Jaljeera" to 30,
            "Sugarcane Juice" to 60,
            "Aam Panna" to 70,
            "Badam Milk" to 80,
            "Filter Coffee" to 60
        ),
        "Rotis" to mapOf(
            "Butter Naan" to 40,
            "Tandoori Roti" to 30,
            "Missi Roti" to 50,
            "Rumali Roti" to 60,
            "Makki di Roti" to 50,
            "Lachha Paratha" to 60
        ),
        "Desserts" to mapOf(
            "Gulab Jamun" to 60,
            "Rasgulla" to 50,
            "Jalebi" to 70,
            "Rabri" to 90,
            "Kaju Katli" to 150,
            "Gajar Ka Halwa" to 120,
            "Basundi" to 80,
            "Puran Poli" to 100
        )
    )

    private var selectedCuisine: String = ""
    private var selectedDish: String? = null
    private val orderList = mutableListOf<String>()
    private var totalAmount = 0 // Store total price

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinnerCuisine = findViewById(R.id.spinnerCuisine)
        listViewDishes = findViewById(R.id.listViewDishes)
        etQuantity = findViewById(R.id.etQuantity)
        btnAddToOrder = findViewById(R.id.btnAddToOrder)
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder)
        tvOrderSummary = findViewById(R.id.tvOrderSummary)
        btnnotes = findViewById(R.id.addnotes)


        btnnotes.setOnClickListener {
            showKotNoteDialog()
        }

        // Setup Cuisine Spinner
        val cuisineList = cuisineMap.keys.toList()
        val cuisineAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cuisineList)
        cuisineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCuisine.adapter = cuisineAdapter

        // Load initial dish list
        loadDishes(cuisineList.first())

        // Spinner selection change listener
        spinnerCuisine.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCuisine = cuisineList[position]
                loadDishes(selectedCuisine)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // ListView Item Click Listener
        listViewDishes.setOnItemClickListener { _, _, position, _ ->
            selectedDish = (listViewDishes.adapter as ArrayAdapter<String>).getItem(position)
            Toast.makeText(this, "Selected: $selectedDish", Toast.LENGTH_SHORT).show()
        }

        // Add to Order Button
        btnAddToOrder.setOnClickListener {
            val quantityStr = etQuantity.text.toString().trim()

            if (selectedDish.isNullOrEmpty()) {
                Toast.makeText(this, "Please select a dish", Toast.LENGTH_SHORT).show()
            } else if (quantityStr.isEmpty() || quantityStr.toIntOrNull() == null || quantityStr.toInt() <= 0) {
                Toast.makeText(this, "Enter valid quantity", Toast.LENGTH_SHORT).show()
            } else {
                val quantity = quantityStr.toInt()
                val dishName = selectedDish!!.split(" - ₹")[0] // Extract dish name only
                val price = cuisineMap[selectedCuisine]?.get(dishName) ?: 0
                val totalPrice = price * quantity

                // Add to order list
                val orderItem = "$dishName x $quantity = ₹$totalPrice"
                orderList.add(orderItem)

                // Update total amount
                totalAmount += totalPrice

                // Update Order Summary
                updateOrderSummary()
                etQuantity.text.clear()
            }
        }

        // Confirm Order Button
        btnConfirmOrder.setOnClickListener {
            if (orderList.isEmpty()) {
                Toast.makeText(this, "No items in the order", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Order Confirmed!", Toast.LENGTH_SHORT).show()
                //orderList.clear()
                // totalAmount = 0 // Reset total amount
                updateOrderSummary()

            }
        }
        btnorderedfood.setOnClickListener {
            if (orderList.isEmpty()) {
                Toast.makeText(this, "No items in the order", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, OrderedFood::class.java)
                intent.putStringArrayListExtra("orderList", ArrayList(orderList))
                intent.putExtra("totalAmount", totalAmount)
                startActivity(intent)
            }
        }

    }

    private fun showKotNoteDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.activity_dialog_kot_note)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout((resources.displayMetrics.widthPixels * 0.85).toInt(), (resources.displayMetrics.heightPixels * 0.4).toInt())

        val etKotNote: EditText = dialog.findViewById(R.id.etKotNote)
        val btnCancel: Button = dialog.findViewById(R.id.btnCancel)
        val btnOk: Button = dialog.findViewById(R.id.btnOk)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnOk.setOnClickListener {
            val note = etKotNote.text.toString().trim()
            if (note.isNotEmpty()) {
                Toast.makeText(this, "Note: $note", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        dialog.show()
    }
    // Load Dishes based on selected cuisine
    private fun loadDishes(cuisineCategory: String) {
        val dishesWithPrices = cuisineMap[cuisineCategory]

        if (dishesWithPrices != null) {
            val dishListWithPrices = dishesWithPrices.map { (dish, price) -> "$dish - ₹$price" }
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, dishListWithPrices)
            listViewDishes.adapter = adapter
        }
    }

    // Update Order Summary
    private fun updateOrderSummary() {
        tvOrderSummary.text = if (orderList.isNotEmpty()) {
            orderList.joinToString("\n") + "\n\nTotal: ₹$totalAmount"
        } else {
            "No items added"
        }
    }
}
