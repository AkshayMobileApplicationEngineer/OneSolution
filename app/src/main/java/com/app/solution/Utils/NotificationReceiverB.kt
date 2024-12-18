package com.app.solution

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationReceiverB : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val CHANNEL_ID = "default_channel"
        val NOTIFICATION_ID = 2

        // Ensure notification channel exists (important for Android 8+)
        createNotificationChannel(context)

        // Define the custom sound URI
        //val soundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val soundUri: Uri = Uri.parse("android.resource://${context.packageName}/raw/${R.raw.iphone_alarm}")
        // Build the notification
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("With Custom Sound")
            .setContentText("This is your daily notification at 22:35 with custom sound!")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setSound(soundUri) // Add custom sound
            .build()

        // Check for notification permission
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // If permission isn't granted, return without showing the notification
            return
        }

        // Show the notification
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel(context: Context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val soundUri: Uri = Uri.parse("android.resource://${context.packageName}/raw/${R.raw.iphone_alarm}")
            val channel = NotificationChannel(
                "default_channel", "Daily Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel for daily reminder notifications"
                setSound(soundUri, null) // Set custom sound for the channel
            }

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
