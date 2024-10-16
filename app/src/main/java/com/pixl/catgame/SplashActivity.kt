package com.pixl.catgame

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.ProgressBar

class SplashActivity : AppCompatActivity() {

    private val splashTimeOut: Long = 5000 // 5 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Show the ProgressBar when the activity is created
        findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE

        // Use a Handler to delay the transition to MainActivity
        Handler().postDelayed({
            // Here you can load your important information, resources, etc.
            loadImportantData()

            // Hide the ProgressBar after loading is complete
            findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Finish the SplashActivity so the user can't go back to it
        }, splashTimeOut)
    }

    private fun loadImportantData() {
        // Simulate loading important data (You can replace this with actual loading logic)
        // e.g., loading from a database, API calls, etc.
    }
}
