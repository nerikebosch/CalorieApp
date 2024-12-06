package com.example.calorieapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calorieapp.auth.AuthManagerSetup
import com.example.calorieapp.homescreen.HomeScreen
import com.example.calorieapp.loginscreens.LoginScreen
import com.example.calorieapp.loginscreens.SignUpScreen
import com.example.calorieapp.ui.theme.CalorieAppTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        val auth = Firebase.auth

        // Create AuthManager and GoogleSignInManager
        val authManagerSetup = AuthManagerSetup(auth, context = this)
        val credentialManager = CredentialManager.create(this)
        val authManager = authManagerSetup.authManager
        val googleSignInManager = authManagerSetup.googleSignInManager
        val emailPasswordAuthManager = authManagerSetup.emailPasswordAuthManager

        setContent {
            CalorieAppTheme {
                // Use the NavController to handle screen navigation
                val navController = rememberNavController()
                val context = LocalContext.current
                val scope = rememberCoroutineScope()

                val startDestination = if (authManager.isUserLoggedIn()) "home" else "login"

                // Set up NavHost to manage navigation
                NavHost(
                    navController = navController,
                    startDestination = startDestination,
                ) {
                    composable("login") {
                        LoginScreen(
                            navController = navController,
                            onGoogleSignInClick = {
                                scope.launch {
                                    googleSignInManager.initiateGoogleSignIn(
                                        scope = scope,
                                        onSuccess = {
                                            navController.popBackStack()
                                            navController.navigate("home")
                                        },
                                        onError = { exception ->
                                            Toast.makeText(
                                                context,
                                                "Sign-in failed: ${exception.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    )
                                }
                           },
                            onEmailPasswordSignInClick = { email, password ->
                                emailPasswordAuthManager.signIn(
                                    email = email,
                                    password = password,
                                    onSuccess = {
                                        navController.popBackStack()
                                        navController.navigate("home")
                                    },
                                    onError = { exception ->
                                        Toast.makeText(
                                            context,
                                            "Sign-in failed: ${exception.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )

                            },
                            onFacebookSignInClick = { /* TODO */ },
                            //onForgotPasswordClick = { /* TODO */ },
                        )
                    }


                    composable("signup") {
                        SignUpScreen(navController)
                    }

                    composable("home") {
                        HomeScreen(
                            navController,
                            currentUser = authManager.getCurrentUser(),
                            onSignOutClick = {
                                scope.launch {
                                    googleSignInManager.signOut(
                                        scope = scope
                                    ) {
                                        navController.navigate("login") {
                                            popUpTo("home") { inclusive = true }
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}