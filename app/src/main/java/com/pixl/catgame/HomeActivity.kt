package com.pixl.catgame

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import android.os.Build

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

        // Check and request notification permission for Android 13 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                // Request the permission
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1
                )
            } else {
                // Permission granted; schedule notifications
                scheduleNotification(this)
            }
        } else {
            // For devices below Android 13, directly schedule notifications
            scheduleNotification(this)
        }
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
