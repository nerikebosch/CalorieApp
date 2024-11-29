package com.example.calorieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.calorieapp.loginscreens.LoginScreen
import com.example.calorieapp.ui.theme.CalorieAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalorieAppTheme {
                LoginScreen()
            }
        }
    }
}

