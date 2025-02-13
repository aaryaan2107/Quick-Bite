package com.example.menu_order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.*
import android.view.animation.AnimationUtils
import android.content.*
import android.widget.*

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val logo: ImageView = findViewById(R.id.logoImage)

        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in)

        logo.startAnimation(fadeIn)
        logo.startAnimation(zoomIn)

        Handler(Looper.getMainLooper()).postDelayed({
            val iLogin = Intent(this, loginpage::class.java)
            startActivity(iLogin)
            finish() // Optional: to close the splash screen activity
        }, 3000)

    }
}
