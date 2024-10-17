package com.pixl.catgame

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val startGameButton = findViewById<Button>(R.id.startGameButton)

        // Navigate to the Game Screen
        startGameButton.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        // Call the scheduleNotification function here to start notifications
        scheduleNotification(this)  // <-- Ensures notification scheduling starts
    }

    // Schedules notifications to trigger periodically (e.g., every 2 hours)
    private fun scheduleNotification(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Set the alarm to fire at a random time (e.g., 2 hours later) and repeat every 2 hours
        val triggerAtMillis = System.currentTimeMillis() + 2 * 60 * 60 * 1000 // 2 hours from now
        val repeatIntervalMillis = 2 * 60 * 60 * 1000 // 2 hours

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP, triggerAtMillis,
            repeatIntervalMillis.toLong(), pendingIntent
        )
    }
}
