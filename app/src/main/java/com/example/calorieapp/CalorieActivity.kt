package com.example.calorieapp

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.app.ActivityCompat
import com.example.calorieapp.notifications.createNotificationChannel
import com.example.calorieapp.notifications.handleNotificationPermission
import com.example.calorieapp.ui.theme.CalorieAppTheme

import dagger.hilt.android.AndroidEntryPoint


//private const val POST_NOTIFICATIONS_PERMISSION = "android.permission.POST_NOTIFICATIONS"

@AndroidEntryPoint
@ExperimentalMaterial3Api()
class CalorieActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //createNotificationChannel(this)

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
//            // Request POST_NOTIFICATIONS permission
//        } else {
//            // No need to request permission, just show the notification
//            showNotification(this)
//        }

        // Create notification channel
        createNotificationChannel(this)

        // Handle permissions and show notification
        handleNotificationPermission(this)

        setContent {
            // Create a NavController to handle navigation
            CalorieAppTheme { CalorieApp() }

        }
    }
}