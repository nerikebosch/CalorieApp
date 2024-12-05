package com.example.calorieapp.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthManager(private val auth: FirebaseAuth) {
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}