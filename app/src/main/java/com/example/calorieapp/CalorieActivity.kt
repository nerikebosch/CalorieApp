package com.example.calorieapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.calorieapp.notifications.createNotificationChannel
import com.example.calorieapp.notifications.handleNotificationPermission
import com.example.calorieapp.ui.theme.CalorieAppTheme

import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity for the Calorie App, responsible for initializing notifications and setting up the UI theme.
 */
@AndroidEntryPoint
@ExperimentalMaterial3Api
class CalorieActivity: AppCompatActivity() {

    /**
     * Called when the activity is starting. This is where most initialization should occur.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in [onSaveInstanceState].
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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