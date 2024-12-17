
package com.app.solution

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Trigger notification scheduling
            val serviceIntent = Intent(context, NotificationServiceB::class.java)
            context.startService(serviceIntent)
        }
    }
}
