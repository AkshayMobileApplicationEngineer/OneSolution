package com.app.solution

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class MainFragment : Fragment() {

    private lateinit var fabAdd: FloatingActionButton
    private val NOTIFICATION_ID = 1
    private val CHANNEL_ID = "default_channel"
    private val TAG = "MainFragment"
    private val PREFS_NAME = "app_prefs"
    private val KEY_NOTIFICATION_PERMISSION = "notification_permission_shown"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e(TAG, "onCreateView called")
        showToast("onCreateView called")

        // Set light status bar for compatibility
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), android.R.color.background_dark)

        // Inflate the layout
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        Log.e(TAG, "View inflated successfully")
        scheduleDailyNotification()
        // Initialize views
        fabAdd = view.findViewById(R.id.fabAdd)
        Log.e(TAG, "FAB initialized")

        // Check and request notification permission
        checkAndRequestNotificationPermission()

        fabAdd.setOnClickListener {
            Log.e(TAG, "FAB clicked")
            showToast("FAB clicked")
            showNotification()
        }

        return view
    }

    private fun checkAndRequestNotificationPermission() {
        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val permissionShown = sharedPreferences.getBoolean(KEY_NOTIFICATION_PERMISSION, false)

        if (!permissionShown) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Check for runtime notification permission
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1001)
                }
            } else {
                // Handle lower Android versions here
                sharedPreferences.edit().putBoolean(KEY_NOTIFICATION_PERMISSION, true).apply()
            }
        }
    }

    private fun showNotification() {
        Log.e(TAG, "showNotification called")
        val intent = Intent(requireContext(), MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_content))
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(requireContext()).notify(NOTIFICATION_ID, notification)
        } else {
            Log.e(TAG, "Notification permission denied")
            showToast("Notification permission denied")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun scheduleDailyNotification() {
        val serviceIntent = Intent(requireContext(), NotificationServiceB::class.java)
        requireContext().startService(serviceIntent)
        showToast("Daily notification scheduled at 22:15")
    }
}
