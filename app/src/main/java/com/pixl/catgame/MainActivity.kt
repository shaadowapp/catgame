package com.pixl.catgame

import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var timerText: TextView
    private lateinit var appleCounter: TextView
    private lateinit var startButton: Button
    private lateinit var catButton: Button
    private lateinit var treeButton: Button
    private lateinit var resultText: TextView

    private var appleCount = 0
    private var timeLeftInMillis: Long = 60000 // 1 minute
    private var gameStarted = false
    private var appleVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        timerText = findViewById(R.id.timerText)
        appleCounter = findViewById(R.id.appleCounter)
        startButton = findViewById(R.id.startButton)
        catButton = findViewById(R.id.catButton)
        treeButton = findViewById(R.id.treeButton)
        resultText = findViewById(R.id.resultText)

        startButton.setOnClickListener {
            if (!gameStarted) {
                startGame()
            }
        }

        catButton.setOnClickListener {
            if (gameStarted && appleVisible) {
                appleCount++
                appleCounter.text = "Apples Eaten: $appleCount"
                if (appleCount >= 100) {
                    resultText.text = "Congratulations! You ate $appleCount apples!"
                    gameStarted = false
                    startButton.text = "Restart Game" // Change button text
                    startButton.visibility = Button.VISIBLE // Show the button
                }
                appleVisible = false // Hide the apple after being eaten
            }
        }

        treeButton.setOnClickListener {
            if (gameStarted) {
                appleVisible = true // Make the apple visible
                appleCounter.text = "Apple Dropped! Click the cat!"
            }
        }
    }

    private fun startGame() {
        gameStarted = true
        appleCount = 0
        appleCounter.text = "Apples Eaten: 0"
        resultText.text = ""
        timeLeftInMillis = 60000 // Reset to 1 minute
        startButton.visibility = Button.GONE // Hide the start button
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
                startButton.text = "Restart Game" // Change button text
                startButton.visibility = Button.VISIBLE // Show the button
            }
        }.start()
    }
}
