package com.pixl.catgame

import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView

class GameActivity : AppCompatActivity() {

    private lateinit var timerText: TextView
    private lateinit var appleCounter: TextView
    private lateinit var catButton: Button
    private lateinit var treeButton: Button
    private lateinit var resultText: TextView
    private lateinit var backToHomeButton: Button
    private lateinit var restartGameButton: Button

    private var appleCount = 0
    private var timeLeftInMillis: Long = 60000 // 1 minute
    private var gameStarted = false
    private var appleVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // Initialize views
        timerText = findViewById(R.id.timerText)
        appleCounter = findViewById(R.id.appleCounter)
        catButton = findViewById(R.id.catButton)
        treeButton = findViewById(R.id.treeButton)
        resultText = findViewById(R.id.resultText)
        backToHomeButton = findViewById(R.id.backToHomeButton)
        restartGameButton = findViewById(R.id.restartGameButton)

        // Start the game automatically
        startGame()

        // Cat button click logic
        catButton.setOnClickListener {
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

        // Tree button click logic
        treeButton.setOnClickListener {
            if (gameStarted) {
                appleVisible = true // Make the apple visible
                appleCounter.text = "Apple Dropped! Click the cat!"
            }
        }

        // Back to Home button click logic
        backToHomeButton.setOnClickListener {
            finish() // Navigate back to the home screen
        }

        // Restart Game button click logic
        restartGameButton.setOnClickListener {
            startGame() // Restart the game
        }
    }

    // Method to start the game
    private fun startGame() {
        gameStarted = true
        appleCount = 0
        appleCounter.text = "Apples Eaten: $appleCount/100" // Change to show 0/100
        resultText.text = ""
        timeLeftInMillis = 60000 // Reset to 1 minute
        startTimer() // Automatically start the timer
        backToHomeButton.visibility = Button.GONE // Hide buttons initially
        restartGameButton.visibility = Button.GONE // Hide buttons initially
    }

    // Countdown timer logic
    private fun startTimer() {
        object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                timerText.text = "Time Left: ${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                gameStarted = false
                resultText.text = "Oops! You only ate $appleCount apples!"
                endGame()
            }
        }.start()
    }

    // Method to handle the end of the game
    private fun endGame() {
        gameStarted = false
        backToHomeButton.visibility = Button.VISIBLE // Show back to home button
        restartGameButton.visibility = Button.VISIBLE // Show restart game button
    }
}
