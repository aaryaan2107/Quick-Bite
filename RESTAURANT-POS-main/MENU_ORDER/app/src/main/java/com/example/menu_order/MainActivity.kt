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
    private lateinit var btnAddNotes: Button
   // private lateinit var btnUpdateOrder: Button
    private lateinit var btnConfirmOrder: Button
    private lateinit var btnOrderedFood: Button
    private lateinit var listViewOrderSummary: ListView
    private lateinit var btnEditOrder: ImageView

    private val cuisineList = arrayOf("Indian", "Chinese", "Italian", "Mexican")
    private val foodItemsMap = mapOf(
        "Indian" to arrayOf("Paneer Butter Masala", "Dal Tadka", "Biryani"),
        "Chinese" to arrayOf("Noodles", "Manchurian", "Fried Rice"),
        "Italian" to arrayOf("Pizza", "Pasta", "Lasagna"),
        "Mexican" to arrayOf("Tacos", "Burritos", "Nachos")
    )

    private val orderList = mutableListOf<String>()
    private lateinit var dishAdapter: ArrayAdapter<String>
    private lateinit var orderAdapter: ArrayAdapter<String>

    private var selectedDish: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI Components
        spinnerCuisine = findViewById(R.id.spinnerCuisine)
        listViewDishes = findViewById(R.id.listViewDishes)
        etQuantity = findViewById(R.id.etQuantity)
        btnAddToOrder = findViewById(R.id.btnAddToOrder)
        btnAddNotes = findViewById(R.id.addnotes)
        //btnUpdateOrder = findViewById(R.id.btnUpdateOrder)
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder)
        btnOrderedFood = findViewById(R.id.btnOrderedFood)
        listViewOrderSummary = findViewById(R.id.listViewOrderSummary)
        btnEditOrder = findViewById(R.id.btnEditOrder)

        // Set up Cuisine Spinner
        val cuisineAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cuisineList)
        spinnerCuisine.adapter = cuisineAdapter

        // Set up Dishes ListView
        dishAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        listViewDishes.adapter = dishAdapter

        // Set up Order ListView
        orderAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, orderList)
        listViewOrderSummary.adapter = orderAdapter

        // Initially disable dish selection and quantity input
        listViewDishes.isEnabled = false
        etQuantity.isEnabled = false

        // Change dishes based on selected cuisine
        spinnerCuisine.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCuisine = cuisineList[position]
                dishAdapter.clear()

                // Enable listViewDishes now that cuisine is selected
                listViewDishes.isEnabled = true
                etQuantity.isEnabled = false // Reset quantity input

                val foodList = foodItemsMap[selectedCuisine]?.toMutableList() ?: mutableListOf()
                dishAdapter.addAll(foodList)
                dishAdapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                listViewDishes.isEnabled = false
                etQuantity.isEnabled = false
            }
        }

        // Select dish from the list
        listViewDishes.setOnItemClickListener { _, _, position, _ ->
            selectedDish = dishAdapter.getItem(position)
            etQuantity.isEnabled = true // Enable quantity input after dish selection
            etQuantity.requestFocus()
        }

        // Handle Add to Order Button Click
        btnAddToOrder.setOnClickListener {
            val cuisine = spinnerCuisine.selectedItem?.toString()
            val quantity = etQuantity.text.toString().trim()

            if (cuisine.isNullOrEmpty()) {
                Toast.makeText(this, "Select a cuisine first!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedDish.isNullOrEmpty()) {
                Toast.makeText(this, "Select a dish first!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (quantity.isEmpty()) {
                Toast.makeText(this, "Enter quantity!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val orderItem = "$selectedDish - $quantity pcs"
            orderList.add(orderItem)
            orderAdapter.notifyDataSetChanged()

            // Reset selection
            etQuantity.text.clear()
            selectedDish = null
            etQuantity.isEnabled = false
        }

        // Handle Edit Order Button Click
        btnEditOrder.setOnClickListener {
            if (orderList.isNotEmpty()) {
                orderList.removeAt(orderList.size - 1) // Removes last item
                orderAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Last item removed", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No items to remove", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle Update Order Button Click
//        btnUpdateOrder.setOnClickListener {
//            Toast.makeText(this, "Order Updated", Toast.LENGTH_SHORT).show()
//        }

        // Handle Confirm Order Button Click
        btnConfirmOrder.setOnClickListener {
            if (orderList.isNotEmpty()) {
                Toast.makeText(this, "Order Confirmed!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "No items in order!", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle Ordered Food Button Click
        btnOrderedFood.setOnClickListener {
            Toast.makeText(this, "Showing Ordered Food", Toast.LENGTH_SHORT).show()
        }

        // Handle Add Notes Button Click
        btnAddNotes.setOnClickListener {
            Toast.makeText(this, "Add Notes Feature Coming Soon!", Toast.LENGTH_SHORT).show()
        }
    }
}
