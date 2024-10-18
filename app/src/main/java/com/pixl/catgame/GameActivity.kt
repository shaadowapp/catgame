package com.pixl.catgame

import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.InputStream
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    private lateinit var timerText: TextView
    private lateinit var appleCounter: TextView
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
            catImage = findViewById(R.id.catImage)
            treeImage = findViewById(R.id.treeImage)

            // Start the game logic
            startGame()

            treeImage.setOnClickListener {
                if (gameStarted) {
                    appleVisible = true
                    appleCounter.text = "Apple Dropped! Click the cat!"
                }
            }

            catImage.setOnClickListener {
                if (gameStarted && appleVisible) {
                    appleCount++
                    appleCounter.text = "Apples Eaten: $appleCount/120"
                    if (appleCount >= 120) {
                        showResult(true)  // Player won
                        endGame()
                    }
                    appleVisible = false
                }
            }

        } catch (e: Exception) {
            Log.e("GameActivity", "Error initializing activity", e)
        }
    }

    private fun startGame() {
        gameStarted = true
        appleCount = 0
        appleCounter.text = "Apples Eaten: 0/120"
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
                showResult(appleCount >= 120) // Check if won
                endGame()
            }
        }.start()
    }

    private fun endGame() {
        gameStarted = false
    }

    private fun showResult(isWin: Boolean) {
        val message = if (isWin) {
            "Congratulations! You ate $appleCount apples!"
        } else {
            "Oops! You only ate $appleCount apples!"
        }

        // Load and display a random comment from JSON
        val comment = if (isWin) {
            getRandomCommentFromJson(R.raw.win_comments)
        } else {
            getRandomCommentFromJson(R.raw.lose_comments)
        }

        val dialogMessage = "$message\n\n$comment"

        // Show result in a popup dialog
        AlertDialog.Builder(this)
            .setTitle(if (isWin) "You Win!" else "You Lose!")
            .setMessage(dialogMessage)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun getRandomCommentFromJson(resourceId: Int): String {
        val json = loadJsonFromResource(resourceId)
        val jsonObject = JSONObject(json)
        val comments = jsonObject.getJSONArray("comments")
        val randomIndex = Random.nextInt(comments.length())
        return comments.getString(randomIndex)
    }

    private fun loadJsonFromResource(resourceId: Int): String {
        val inputStream: InputStream = resources.openRawResource(resourceId)
        return inputStream.bufferedReader().use { it.readText() }
    }
}
