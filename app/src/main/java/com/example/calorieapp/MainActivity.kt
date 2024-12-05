package com.example.calorieapp

import android.os.Bundle
import android.util.Log.e
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calorieapp.homescreen.HomeScreen
import com.example.calorieapp.loginscreens.LoginScreen
import com.example.calorieapp.loginscreens.SignUpScreen
import com.example.calorieapp.ui.theme.CalorieAppTheme
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch


const val WEB_CLIENT_ID = "501150034108-c1b0su0oha3l6bqs0jbgkib8iqfouc7c.apps.googleusercontent.com"

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        setContent {
            CalorieAppTheme {
                // Use the NavController to handle screen navigation
                val navController = rememberNavController()

                val context = LocalContext.current
                val scope = rememberCoroutineScope()
                val credentialManager = CredentialManager.create(context)

                val startDestination = if (auth.currentUser == null) "login" else "home"

                // Set up NavHost to manage navigation
                NavHost(
                    navController = navController,
                    startDestination = startDestination,  // Starting screen
                ) {
                    composable("login") {
                        LoginScreen(
                            navController,
                            onSignInClick = {
                                val googleIdOption = GetGoogleIdOption.Builder()
                                    .setFilterByAuthorizedAccounts(false)
                                    .setServerClientId(WEB_CLIENT_ID)
                                    .build()

                                val request = GetCredentialRequest.Builder()
                                    .addCredentialOption(googleIdOption)
                                    .build()

                                scope.launch {
                                    try {
                                        val result = credentialManager.getCredential(
                                            request = request,
                                            context = context
                                        )
                                        val credential = result.credential
                                        val googleIdTokenCredential = GoogleIdTokenCredential
                                            .createFrom(credential.data)
                                        val googleIdToken = googleIdTokenCredential.idToken

                                        val firebaseCredential =
                                            GoogleAuthProvider.getCredential(googleIdToken, null)
                                        auth.signInWithCredential(firebaseCredential)
                                            .addOnCompleteListener{ task ->
                                                if (task.isSuccessful) {
                                                    navController.popBackStack()
                                                    navController.navigate("home")
                                                } else {
                                                    e("Login", "signInWithCredential:failure", task.exception)
                                                }
                                            }
                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            context,
                                            "Error: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        e.printStackTrace()
                                    }
                                }

                            }
                        ) // Pass navController to LoginScreen
                    }
                    composable("signup") {
                        SignUpScreen(navController) // The SignUpScreen doesn't need navController
                    }

                    composable("home") {
                        HomeScreen(
                            navController,
                            currentUser = auth.currentUser,
                            onSignOutClick = {
                                auth.signOut()
                                scope.launch {
                                    credentialManager.clearCredentialState(
                                        ClearCredentialStateRequest()
                                    )
                                }
                                navController.popBackStack()
                                navController.navigate("login")
                            }
                        )
                    }
                }
            }
        }
    }
}