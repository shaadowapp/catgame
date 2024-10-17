package com.pixl.catgame

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView

class GameActivity : AppCompatActivity() {

    private lateinit var timerText: TextView
    private lateinit var appleCounter: TextView
    private lateinit var resultText: TextView
    private lateinit var catImage: ImageView
    private lateinit var treeImage: ImageView

    private var appleCount = 0
    private var timeLeftInMillis: Long = 60000 // 1 minute
    private var gameStarted = false
    private var appleVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_game)


            // Initialize views
            timerText = findViewById(R.id.timerText)
            appleCounter = findViewById(R.id.appleCounter)
            resultText = findViewById(R.id.resultText)
            catImage = findViewById(R.id.catImage)
            treeImage = findViewById(R.id.treeImage)

            // Set onClick listeners for images
            treeImage.setOnClickListener {
                if (gameStarted) {
                    appleVisible = true // Make the apple visible
                    appleCounter.text = "Apple Dropped! Click the cat!"
                }
            }

            catImage.setOnClickListener {
                if (gameStarted && appleVisible) {
                    appleCount++
                    appleCounter.text = "Apples Eaten: $appleCount/100"
                    if (appleCount >= 100) {
                        resultText.text = "Congratulations! You ate $appleCount apples!"
                        endGame()
                    }
                    appleVisible = false // Hide the apple after being eaten
                }
            }

            // Start the game automatically when coming from home
            startGame()
        }catch (e: Exception) {
            Log.e("GameActivity", "Error initializing activity", e)
        }
    }

    private fun startGame() {
        gameStarted = true
        appleCount = 0
        appleCounter.text = "Apples Eaten: 0/100"
        resultText.text = ""
        timeLeftInMillis = 60000 // Reset to 1 minute
        startTimer()
    }

    private fun startTimer() {
        object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                timerText.text = "Time Left: ${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                gameStarted = false
                if (appleCount < 100) {
                    resultText.text = "Oops! You only ate $appleCount apples!"
                }
                // Show back to home and restart buttons here
                showResultButtons()
            }
        }.start()
    }

    private fun endGame() {
        gameStarted = false
        // Show back to home and restart buttons here
        showResultButtons()
    }

    private fun showResultButtons() {
        // Logic to show "Back to Home" and "Restart Game" buttons
        // You can create buttons dynamically or make them visible here
    }
}
