package com.example.calorieapp.loginscreens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SignUpViewModel {
    // State variables for form fields and error handling
    var name by mutableStateOf("")
    var surname by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var errorMessage by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)

    suspend fun onSignUpClick() {
        // Input validation
        if (name.isBlank() || surname.isBlank() || email.isBlank() || password.isBlank()) {
            errorMessage = "All fields are required"
            return
        }

        // Reset previous error
        errorMessage = null
        isLoading = true

        try {
            // Firebase Authentication signup
            val authResult = FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .await()

            // Get the current user
            val firebaseUser = authResult.user ?: throw Exception("User creation failed")

            // Create user document in Firestore
            val userDocument = hashMapOf(
                "firstName" to name,
                "lastName" to surname,
                "email" to email,
                "createdAt" to com.google.firebase.Timestamp.now()
            )

            // Save additional user details to Firestore
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(firebaseUser.uid)
                .set(userDocument)
                .await()

            // Signup successful
            // Navigate or perform post-signup actions
        } catch (e: Exception) {
            // Handle specific Firebase authentication errors
            errorMessage = when (e) {
                is com.google.firebase.auth.FirebaseAuthWeakPasswordException ->
                    "Password is too weak. Please use a stronger password."
                is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException ->
                    "Invalid email format. Please check your email."
                is com.google.firebase.auth.FirebaseAuthUserCollisionException ->
                    "Email is already in use. Please use a different email."
                else -> "Signup failed: ${e.localizedMessage}"
            }
        } finally {
            isLoading = false
        }
    }
}