package com.example.calorieapp.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.calorieapp.model.service.AccountService
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class GoogleSignInManager(
    private val context: Context,
    private val accountService: AccountService,
    //private val auth: FirebaseAuth,
    private val credentialManager: CredentialManager,
    private val webClientId: String
) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    /**
     * Initiates Google Sign-In process
     * @param scope CoroutineScope for launching coroutines
     * @param onSuccess Callback for successful sign-in
     * @param onError Callback for sign-in errors
     */
    suspend fun initiateGoogleSignIn(
        //scope: CoroutineScope,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        try {
            // Configure Google Sign-In options
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(webClientId)
                .build()

            // Create credential request
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            // Get credentials
            val result = credentialManager.getCredential(
                request = request,
                context = context
            )

            val googleIdTokenCredential = GoogleIdTokenCredential
                .createFrom((result.credential as CustomCredential).data)
            val googleIdToken = googleIdTokenCredential.idToken

            accountService.signInWithGoogle(googleIdToken) // Use AccountService here

            onSuccess()
            // Process the credential
            processGoogleSignInCredential(result.credential as CustomCredential, onSuccess, onError)

        } catch (e: Exception) {
            Log.e("GoogleSignInManager", "Sign-in error", e)
            onError(e)
        }
    }

    /**
     * Processes the obtained Google Sign-In credential
     * @param credential Obtained credential
     * @param onSuccess Callback for successful sign-in
     * @param onError Callback for sign-in errors
     */
    private suspend fun processGoogleSignInCredential(
        credential: CustomCredential,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        try {
            // Extract Google ID Token
            val googleIdTokenCredential = GoogleIdTokenCredential
                .createFrom(credential.data)
            val googleIdToken = googleIdTokenCredential.idToken

            // Create Firebase credential
            val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)

            // Sign in to Firebase
            val authResult = auth.signInWithCredential(firebaseCredential).await()

            // Check if this is a new user
            val isNewUser = authResult.additionalUserInfo?.isNewUser == true


            // Invoke success callback
            onSuccess()

        } catch (e: Exception) {
            Log.e("GoogleSignInManager", "Firebase sign-in error", e)
            onError(e)
        }
    }


    /**
     * Retrieves additional user information after sign-in
     * @return Map of user details
     */
    fun getUserDetails(): Map<String, Any?> {
        val user = auth.currentUser
        return mapOf(
            "uid" to user?.uid,
            "name" to user?.displayName,
            "email" to user?.email,
            "photoUrl" to user?.photoUrl?.toString()
        )
    }

    /**
     * Checks if the user is currently signed in
     * @return Boolean indicating sign-in status
     */
    fun isSignedIn(): Boolean {
        return auth.currentUser != null
    }
}