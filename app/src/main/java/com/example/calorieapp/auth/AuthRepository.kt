package com.example.calorieapp.auth

import android.content.Context
import androidx.credentials.CredentialManager
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth,
    private val context: Context,
    private val credentialManager: CredentialManager
) {
    // Email and Password Login
    fun loginWithEmailPassword(email: String, password: String): Flow<AuthResult> = flow {
        emit(AuthResult.Loading)
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            emit(AuthResult.Success(result.user))
        } catch (e: Exception) {
            emit(AuthResult.Error(e.message ?: "Login failed"))
        }
    }

    // Email and Password Registration
    fun registerWithEmailPassword(email: String, password: String): Flow<AuthResult> = flow {
        emit(AuthResult.Loading)
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            emit(AuthResult.Success(result.user))
        } catch (e: Exception) {
            emit(AuthResult.Error(e.message ?: "Registration failed"))
        }
    }

    // Google Sign-In
    fun signInWithGoogle(credential: AuthCredential): Flow<AuthResult> = flow {
        emit(AuthResult.Loading)
        try {
            val result = auth.signInWithCredential(credential).await()
            emit(AuthResult.Success(result.user))
        } catch (e: Exception) {
            emit(AuthResult.Error(e.message ?: "Google Sign-In failed"))
        }
    }

    // Facebook Sign-In (placeholder for future implementation)
    fun signInWithFacebook(credential: AuthCredential): Flow<AuthResult> = flow {
        emit(AuthResult.Loading)
        try {
            val result = auth.signInWithCredential(credential).await()
            emit(AuthResult.Success(result.user))
        } catch (e: Exception) {
            emit(AuthResult.Error(e.message ?: "Facebook Sign-In failed"))
        }
    }

    // Get current user
    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    // Sign out
    fun signOut() {
        auth.signOut()
    }

    // Check if user is logged in
    fun isUserLoggedIn(): Boolean = auth.currentUser != null
}