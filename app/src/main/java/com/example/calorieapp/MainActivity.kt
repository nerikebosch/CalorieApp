package com.example.calorieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.calorieapp.loginscreens.LoginScreen
import com.example.calorieapp.loginscreens.SignUpScreen
import com.example.calorieapp.ui.theme.CalorieAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalorieAppTheme {
                // Use the NavController to handle screen navigation
                val navController = rememberNavController()

                // Set up NavHost to manage navigation
                NavHost(
                    navController = navController,
                    startDestination = "login"  // Starting screen
                ) {
                    composable("login") {
                        LoginScreen(navController) // Pass navController to LoginScreen
                    }
                    composable("signup") {
                        SignUpScreen(navController) // The SignUpScreen doesn't need navController
                    }
                }
            }
        }
    }
}