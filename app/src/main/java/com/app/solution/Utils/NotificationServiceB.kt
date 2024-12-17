package com.app.solution

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.app.Service
import android.net.Uri
import androidx.core.app.NotificationCompat
import java.util.*

class NotificationServiceB : Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        scheduleDailyNotification()
        return START_NOT_STICKY
    }

    private fun scheduleDailyNotification() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // Set time for 22:15
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 22)
            set(Calendar.MINUTE, 42)
            set(Calendar.SECOND, 0)
        }

        // If the time is already past for today, set it for tomorrow
        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        // Schedule the alarm
//        alarmManager.setRepeating(
//            AlarmManager.RTC_WAKEUP,
//            calendar.timeInMillis,
//            AlarmManager.INTERVAL_DAY, // Repeat daily
//            pendingIntent
//        )



        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY, // Repeat daily
            pendingIntent
        )

        val notificationIntent = Intent(this, NotificationReceiver::class.java)
        val notificationPendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        val alarmSound = Uri.parse("android.resource://" + packageName + "/" + R.raw.iphone_alarm)
        val notification = NotificationCompat.Builder(this, "default_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Daily Reminder")
            .setContentText("This is your daily notification at 22:35!")
            .setSound(alarmSound)
            .setContentIntent(notificationPendingIntent)
            .setAutoCancel(true)
            .build()

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY, // Repeat daily
            notificationPendingIntent
        )
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
