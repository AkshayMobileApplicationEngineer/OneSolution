package com.app.solution

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "Alarm Triggered!", Toast.LENGTH_SHORT).show()
        Log.d("AlarmReceiver", "Alarm triggered at: ${System.currentTimeMillis()}")

        try {
            mediaPlayer = MediaPlayer.create(context, R.raw.iphone_alarm)
            mediaPlayer?.start()
            Log.d("AlarmReceiver", "Playing alarm sound")
            mediaPlayer?.setOnCompletionListener {
                it.release()
                mediaPlayer = null
                Log.d("AlarmReceiver", "Alarm sound finished, MediaPlayer released")
            }

        } catch (e: Exception)
        {
            Log.e("AlarmReceiver", "Error playing sound", e)
            Toast.makeText(context, "Error Playing Sound", Toast.LENGTH_SHORT).show()
        }


    }
}