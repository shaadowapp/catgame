package com.pixl.catgame

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import org.json.JSONObject
import java.io.InputStream
import kotlin.random.Random

class NotificationHelper(private val context: Context) {

    private val channelId = "catgame_notifications"

    // Create the notification channel for devices with Android 8.0 (API 26) or higher
    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Game Notifications"
            val descriptionText = "Notifications from C.A.T. Game"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Display the notification
    fun showNotification() {
        // Check if the app has the required permission to post notifications (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is not granted, you should handle this scenario (e.g., request permission)
                return
            }
        }

        val notificationMessage = getRandomNotificationMessage()

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)  // Change to your app's notification icon
            .setContentTitle("C.A.T. Game")
            .setContentText(notificationMessage)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            // Show the notification
            notify(Random.nextInt(), builder.build())  // Random ID to avoid overwriting notifications
        }
    }

    // Load a random notification message from the JSON file
    private fun getRandomNotificationMessage(): String {
        val inputStream: InputStream = context.resources.openRawResource(R.raw.notification_messages)
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val jsonObject = JSONObject(jsonString)
        val notificationsArray = jsonObject.getJSONArray("notifications")
        val randomIndex = Random.nextInt(notificationsArray.length())
        return notificationsArray.getString(randomIndex)
    }
}
