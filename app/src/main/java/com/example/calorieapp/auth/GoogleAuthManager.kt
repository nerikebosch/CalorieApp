package com.example.calorieapp.auth

import android.R.attr.data
import android.app.Activity
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
        activity: Activity,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        try {
            // Configure Google Sign-In options
            val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(true)
                .setServerClientId(webClientId)
                .setAutoSelectEnabled(true)
                .build()

            // Create credential request
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            // Get credentials
            val result = credentialManager.getCredential(
                request = request,
                context = activity
            )

            val credential = result.credential
            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential
                    .createFrom((result.credential as CustomCredential).data)

                val googleIdToken = googleIdTokenCredential.idToken

                accountService.signInWithGoogle(googleIdToken) // Use AccountService here

                authenticateWithFirebase(googleIdToken)

                onSuccess()
            } else {
                Log.e("GoogleSignIn", "Unsupported credential type: ${credential.type}")
                onError(Exception("Unsupported credential type"))
            }
            // Process the credential
            // processGoogleSignInCredential(result.credential as CustomCredential, onSuccess, onError)

        } catch (e: Exception) {
            Log.e("GoogleSignInManager", "Sign-in error", e)
            onError(e)
        }
    }

    /**
     * Optionally authenticate with Firebase using the Google ID Token.
     * @param googleIdToken The Google ID Token obtained from credentials.
     */
    private suspend fun authenticateWithFirebase(googleIdToken: String) {
        //val googleCredential = oneTapClient.getSignInCredentialFromIntent(data)
        //val idToken = googleCredential.googleIdToken
        try {
            val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
            auth.signInWithCredential(firebaseCredential).await() // Coroutine-safe await
            Log.d("GoogleSignInManager", "Firebase authentication successful")
        } catch (e: Exception) {
            Log.e("GoogleSignInManager", "Firebase authentication error", e)
            throw e // Propagate the error if needed
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
        )
    }



}