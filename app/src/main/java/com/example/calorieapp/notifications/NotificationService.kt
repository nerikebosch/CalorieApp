package com.example.calorieapp.notifications

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

const val NOTIFICATION_ID = 1 // Unique ID for the notification

fun sendMealNotification(context: Context, builder: NotificationCompat.Builder) {
    with(NotificationManagerCompat.from(context)) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Request POST_NOTIFICATIONS permission if needed
            return
        }
        // Send the notification
        notify(NOTIFICATION_ID, builder.build())
    }
}
