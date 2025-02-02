package com.example.calorieapp.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * BroadcastReceiver that triggers a meal reminder notification when the scheduled alarm fires.
 */
class MealReminderReceiver : BroadcastReceiver() {
    /**
     * Called when the BroadcastReceiver receives an Intent broadcast.
     *
     * @param context The Context in which the receiver is running.
     * @param intent The Intent being received.
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            showNotification(context) // Call the function to show the notification
        }
    }
}