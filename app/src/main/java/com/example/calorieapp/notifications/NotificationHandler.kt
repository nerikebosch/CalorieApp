package com.example.calorieapp.notifications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.calorieapp.CalorieActivity
import com.example.calorieapp.R


/**
 * Constants for notification handling.
 */
private const val CHANNEL_ID = "meal_reminder_channel"
private const val MEAL_NOTIFICATION_ID = 1001
private const val POST_NOTIFICATIONS_PERMISSION = "android.permission.POST_NOTIFICATIONS"

/**
 * Creates a notification channel for meal reminders.
 * This function should be called once during the app lifecycle.
 *
 * @param context The application context used to access system services.
 */
// Function to create the notification channel (called once during app lifecycle)
fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Meal Reminder"
        val description = "Reminders to log your meals in the app."
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            this.description = description
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}


/**
 * Displays a meal reminder notification to the user.
 *
 * @param context The application context used to build and show the notification.
 */
@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
fun showNotification(context: Context) {
    // Intent to open the app when notification is tapped
    val intent = Intent(context, CalorieActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_IMMUTABLE
    )

    // Build the notification
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.notification_icon) // Replace with your app's icon
        .setContentTitle("Don't forget to log your meals!")
        .setContentText("Log your breakfast, lunch, or dinner for today.")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true) // Notification is dismissed when tapped

    // Show the notification
    with(NotificationManagerCompat.from(context)) {
        notify(MEAL_NOTIFICATION_ID, builder.build())
    }
}

/**
 * Checks and requests the necessary permission to show notifications on Android 13+.
 * If permission is already granted, it schedules meal reminders.
 *
 * @param context The application context used for permission checking and scheduling.
 */
// Function to check and request POST_NOTIFICATIONS permission (Android 13+)
fun handleNotificationPermission(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ActivityCompat.checkSelfPermission(context, POST_NOTIFICATIONS_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                (context as? androidx.appcompat.app.AppCompatActivity) ?: return,
                arrayOf(POST_NOTIFICATIONS_PERMISSION),
                1
            )
        } else {
            scheduleMealReminders(context) // Schedule notifications after permission is granted
        }
    } else {
        scheduleMealReminders(context) // Directly schedule notifications for Android 12 and below
    }
}
