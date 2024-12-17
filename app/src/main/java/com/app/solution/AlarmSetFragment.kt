package com.app.solution

import android.Manifest
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.util.*

class AlarmSetFragment : Fragment() {

    private lateinit var datePicker: DatePicker
    private lateinit var timePicker: TimePicker
    private lateinit var setAlarmButton: Button
    private lateinit var calendar: Calendar

    private val alarmManagerPermission = Manifest.permission.SCHEDULE_EXACT_ALARM
    private val REQUEST_PERMISSION_CODE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_alarm_set, container, false)

        datePicker = rootView.findViewById(R.id.date_picker)
        timePicker = rootView.findViewById(R.id.time_picker)
        setAlarmButton = rootView.findViewById(R.id.set_alarm_button)

        setAlarmButton.setOnClickListener {
            calendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, datePicker.year)
                set(Calendar.MONTH, datePicker.month)
                set(Calendar.DAY_OF_MONTH, datePicker.dayOfMonth)
                set(Calendar.HOUR_OF_DAY, timePicker.currentHour)
                set(Calendar.MINUTE, timePicker.currentMinute)
                set(Calendar.SECOND, 0)
            }

            Log.d("AlarmSetFragment", "Alarm set for: ${calendar.time}")
            Toast.makeText(requireContext(), "Alarm set for: ${calendar.time}", Toast.LENGTH_SHORT).show()

            checkPermissionsAndSetAlarm()
        }
        return rootView
    }

    private fun checkPermissionsAndSetAlarm() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            if (ContextCompat.checkSelfPermission(requireContext(), alarmManagerPermission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(),arrayOf(alarmManagerPermission),REQUEST_PERMISSION_CODE)

            } else {
                setAlarm()
                Log.d("checkPermissionsAndSetAlarm", "Alarm set for: ${calendar.time}")
            }
        } else {
            setAlarm()
            Log.d("checkPermissionsAndSetAlarm", "Alarm set for: ${calendar.time}")
        }
    }

    private fun setAlarm() {
        try {
            val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(requireContext(), AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                requireContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            Log.d("AlarmSetFragment", "Scheduling alarm for: ${calendar.timeInMillis}")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
            Log.d("AlarmSetFragment", "Alarm successfully scheduled for: ${calendar.timeInMillis}")
            Toast.makeText(requireContext(), "Alarm successfully scheduled", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("AlarmSetFragment", "Error scheduling alarm", e)
            Toast.makeText(requireContext(), "Failed to set alarm. Please try again.", Toast.LENGTH_SHORT).show()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setAlarm()
            } else {

                // Permission denied, show an alert dialog
                AlertDialog.Builder(requireContext())
                    .setTitle("Permissions Required")
                    .setMessage("Please grant the required permissions to set an alarm.")
                    .setPositiveButton("OK") { _, _ ->

                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + requireActivity().packageName)
                        )
                        intent.addCategory(Intent.CATEGORY_DEFAULT)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                        startActivity(intent)
                    }
                    .setNegativeButton("Cancel") { _, _ ->
                        // Handle the cancel action
                        Toast.makeText(
                            requireContext(),
                            "Permission required to set an alarm",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .show()
                Log.e("AlarmSetFragment", "Permission denied to set exact alarm")
            }
        }
    }
}