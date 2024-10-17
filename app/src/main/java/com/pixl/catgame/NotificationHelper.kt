package com.pixl.catgame

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.pixl.catgame.HomeActivity
import org.json.JSONObject
import java.io.InputStream
import java.util.*

class NotificationHelper(private val context: Context) {

    private fun loadNotificationMessages(): Array<String> {
        // Load the JSON file from the raw resource
        val inputStream: InputStream = context.resources.openRawResource(R.raw.notification_messages)
        val jsonString = inputStream.bufferedReader().use { it.readText() }

        // Parse JSON data
        val jsonObject = JSONObject(jsonString)
        val messagesArray = jsonObject.getJSONArray("messages")

        // Convert the JSON array to a Kotlin array
        return Array(messagesArray.length()) { messagesArray.getString(it) }
    }

    fun sendRandomNotification() {
        // Load messages from the JSON file
        val notificationMessages = loadNotificationMessages()

        // Pick a random message
        val randomMessage = notificationMessages[Random().nextInt(notificationMessages.size)]

        // Create a NotificationManager
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel (required for Android 8.0 and above)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel("catgame_channel", "Game Notifications", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        // Intent to launch the HomeActivity when notification is clicked
        val intent = Intent(context, HomeActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // Build the notification
        val notification = NotificationCompat.Builder(context, "catgame_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)  // Update with your icon
            .setContentTitle("C.A.T. Game")
            .setContentText(randomMessage)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        // Send the notification
        notificationManager.notify(1, notification)
    }
}
