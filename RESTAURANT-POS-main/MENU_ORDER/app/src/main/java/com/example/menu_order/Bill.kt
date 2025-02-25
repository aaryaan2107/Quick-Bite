package com.example.menu_order

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_bill.*
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class Bill : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill)

        val tvBill: TextView = findViewById(R.id.tvBill)
        val etPhoneNumber: EditText = findViewById(R.id.etCustomerPhone)
        val btnSendSMS: Button = findViewById(R.id.btnsms)
        val btnGeneratePDF: Button = findViewById(R.id.btnGeneratePDF)
       // val btnShare: Button = findViewById(R.id.btnShare)

        val db: SQLiteDatabase = openOrCreateDatabase("QUICKBITE", Context.MODE_PRIVATE, null)
        val tableId: Int = intent.getIntExtra("TABLE_ID", 0)

//        db.execSQL("""
//    CREATE TABLE IF NOT EXISTS tbl_Bill (
//        BillID INTEGER PRIMARY KEY AUTOINCREMENT,
//        TableID INTEGER,
//        SessionID INTEGER,
//        OrderID INTEGER,
//        TotalAmount INTEGER,
//        BillDateTime TEXT
//    );
//""")

        // Insert Bill Data
        db.execSQL("""
    INSERT INTO tbl_Bill (TableID, SessionID, OrderID, TotalAmount, BillDateTime)
    SELECT o.TableID, o.SessionID, o.OrderID, SUM(od.Total), o.OrderDateTime
    FROM tbl_Orders o
    JOIN tbl_OrderDetails od ON o.OrderID = od.OrderID
    WHERE o.TableID = ?
    GROUP BY o.SessionID, o.OrderID;
""", arrayOf(tableId.toString()))


        // Fetch Bill Data

        val sessionCursor: Cursor = db.rawQuery(
            "SELECT MAX(SessionID) FROM tbl_orders WHERE TableID = ?",
            arrayOf(tableId.toString())
        )

        var sessionId = 0
        if (sessionCursor.moveToFirst()) {
            sessionId = sessionCursor.getInt(0)
        }
        sessionCursor.close()

        val sb = StringBuilder()
        sb.append("===== Restaurant Bill =====\n\n")

// Fetch Bill Data using SessionID
        val billCursor: Cursor = db.rawQuery(
            "SELECT BillID, TableID, TotalAmount, BillDateTime FROM tbl_Bill WHERE TableID = ? AND SessionID = ?",
            arrayOf(tableId.toString(), sessionId.toString())
        )

        if (billCursor.moveToFirst()) {
            val billId = billCursor.getInt(0)
            val totalAmount = billCursor.getInt(2)
            val billDateTime = billCursor.getString(3)

            sb.append("Table: $tableId\nSession ID: $sessionId\nDate: $billDateTime\n\nItem Details:\n")

            // Fetch Order Details using SessionID
            val detailsCursor: Cursor = db.rawQuery(
                "SELECT ItemName, Quantity, Price, Total FROM tbl_OrderDetails WHERE SessionID = ?",
                arrayOf(sessionId.toString())
            )

            while (detailsCursor.moveToNext()) {
                val itemName = detailsCursor.getString(0)
                val quantity = detailsCursor.getInt(1)
                val price = detailsCursor.getInt(2)
                val total = detailsCursor.getInt(3)
                sb.append("$itemName  x$quantity  ₹$price  = ₹$total\n")
            }
            detailsCursor.close()

            sb.append("\n----------------------\nTotal Amount: ₹$totalAmount\n=======================\n")
        }

        billCursor.close()
        val billText = sb.toString()
        tvBill.text = billText

        // Generate PDF
        btnGeneratePDF.setOnClickListener {
            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas
            val paint = Paint()

            paint.textSize = 14f
            paint.color = Color.BLACK
            val x = 10f
            var y = 25f

            billText.lines().forEach {
                canvas.drawText(it, x, y, paint)
                y += 20
            }

            pdfDocument.finishPage(page)

            val file = File(getExternalFilesDir(null), "Bill.pdf")
            val outputStream = FileOutputStream(file)
            pdfDocument.writeTo(outputStream)
            pdfDocument.close()

            Toast.makeText(this, "PDF Saved: ${file.absolutePath}", Toast.LENGTH_LONG).show()
//        }
//        btnsms.setOnClickListener {
//            val phoneNumber = etPhoneNumber.text.toString()
//            if (phoneNumber.isNotEmpty()) {
//                if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
//                    val smsManager: SmsManager = SmsManager.getDefault()
//                    val a = smsManager.sendTextMessage(phoneNumber, null, billText, null, null)
////                    Log.e("ajhgdf",a.toString())
//                    Toast.makeText(this, "Bill sent to $phoneNumber", Toast.LENGTH_SHORT).show()
//                } else {
//                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), 1)
//                }
//            } else {
//                Toast.makeText(this, "Enter a phone number", Toast.LENGTH_SHORT).show()
//            }
        }
        btnSendSMS.setOnClickListener {
            val phoneNumber = etPhoneNumber.text.toString().trim()
            val billText = tvBill.text.toString()

            if (phoneNumber.isNotEmpty() && billText.isNotEmpty()) {
                sendSms(phoneNumber, billText)
            } else {
                Toast.makeText(this, "Enter phone number and ensure bill is generated", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendSms(phoneNumber: String, message: String) {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Toast.makeText(this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to send SMS: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    // Order Food Button Logic
    private fun orderFood(tableId: Int, selectedOrders: MutableList<Triple<String, Int, Int>>, db: SQLiteDatabase) {
        if (selectedOrders.isEmpty()) {
            Toast.makeText(this, "No order to place!", Toast.LENGTH_SHORT).show()
            return
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val orderDateTime: String = dateFormat.format(Date())
        var totalAmount = 0
        for (order in selectedOrders) {
            totalAmount += order.second * order.third
        }

        // Fetch the latest SessionID for this TableID
        val sessionCursor = db.rawQuery("SELECT MAX(SessionID) FROM tbl_orders WHERE TableID = ?", arrayOf(tableId.toString()))

        var sessionId = 0
        if (sessionCursor.moveToFirst()) {
            sessionId = sessionCursor.getInt(0) // Get the latest session
        }
        sessionCursor.close()

        // If no active session, create a new one
        if (sessionId == 0) {
            sessionId = System.currentTimeMillis().toInt() // Unique session ID
        }

        // Insert the Order with SessionID
        db.execSQL("INSERT INTO tbl_orders (TableID, SessionID, OrderDateTime, TotalAmount) VALUES ($tableId, $sessionId, '$orderDateTime', $totalAmount)")

        val orderCursor = db.rawQuery("SELECT last_insert_rowid()", null)
        orderCursor.moveToFirst()
        val orderId = orderCursor.getInt(0)
        orderCursor.close()

        // Insert Order Details with SessionID
        for ((itemName, price, qty) in selectedOrders) {
            val itemCursor = db.rawQuery("SELECT ItemID FROM tbl_menuitems WHERE ItemName = ?", arrayOf(itemName))
            if (itemCursor.moveToFirst()) {
                val itemId = itemCursor.getInt(0)
                db.execSQL("INSERT INTO tbl_orderdetails (TableID, SessionID, OrderID, ItemID, ItemName, Quantity, Price, Total) VALUES ($tableId, $sessionId, $orderId, $itemId, '$itemName', $qty, $price, ${price * qty})")
            }
            itemCursor.close()
        }

        selectedOrders.clear()
        Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show()
    }
}
