package com.example.menu_order

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream

class Bill : AppCompatActivity() {
    data class OrderItem(val itemName: String, val quantity: Int, val price: Double) {
        fun getTotal(): Double = quantity * price
    }
    private lateinit var tvBill: TextView
    private lateinit var btnGeneratePDF: Button
    private lateinit var btnShare: Button

    private val orderList = listOf(
        OrderItem("Paneer Butter Masala", 2, 250.0),
        OrderItem("Tandoori Roti", 4, 40.0),
        OrderItem("Masala Dosa", 1, 180.0)
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill)
        tvBill = findViewById(R.id.tvBill)
        btnGeneratePDF = findViewById(R.id.btnGeneratePDF)
        btnShare = findViewById(R.id.btnShare)

        val billText = generateBill(orderList)
        tvBill.text = billText

        btnGeneratePDF.setOnClickListener { generatePDF(this, billText) }
        btnShare.setOnClickListener { shareBill(this, billText) }
    }
    private fun generateBill(orderList: List<OrderItem>): String {
        val sb = StringBuilder()
        sb.append("===== Restaurant Bill =====\n\n")

        var totalAmount = 0.0
        for (item in orderList) {
            val itemTotal = item.getTotal()
            sb.append("${item.itemName}  x${item.quantity}  ₹${item.price}  = ₹$itemTotal\n")
            totalAmount += itemTotal
        }

        sb.append("\n----------------------\n")
        sb.append("Total Amount: ₹$totalAmount\n")
        sb.append("=======================\n")

        return sb.toString()
    }

    private fun generatePDF(context: Context, billContent: String) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()

        paint.textSize = 14f
        paint.color = Color.BLACK
        val x = 10f
        var y = 25f

        billContent.lines().forEach {
            canvas.drawText(it, x, y, paint)
            y += 20
        }

        pdfDocument.finishPage(page)

        val file = File(context.getExternalFilesDir(null), "Bill.pdf")
        val outputStream = FileOutputStream(file)
        pdfDocument.writeTo(outputStream)
        pdfDocument.close()

        Toast.makeText(context, "PDF Saved: ${file.absolutePath}", Toast.LENGTH_LONG).show()
    }

    private fun shareBill(context: Context, billText: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, billText)
        context.startActivity(Intent.createChooser(intent, "Share Bill"))
    }
}
