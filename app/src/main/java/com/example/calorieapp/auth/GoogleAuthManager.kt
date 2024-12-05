package com.example.calorieapp.auth

import android.content.Context
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class GoogleSignInManager(
    private val context: Context,
    private val auth: FirebaseAuth,
    private val credentialManager: CredentialManager,
    private val webClientId: String
) {
    fun initiateGoogleSignIn(
        scope: CoroutineScope,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(webClientId)
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

                val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                auth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            onSuccess()
                        } else {
                            onError(task.exception ?: Exception("Unknown error"))
                        }
                    }
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Error: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
                onError(e)
            }
        }
    }

    fun signOut(
        scope: CoroutineScope,
        onSignOutComplete: () -> Unit
    ) {
        auth.signOut()
        scope.launch {
            credentialManager.clearCredentialState(
                androidx.credentials.ClearCredentialStateRequest()
            )
            onSignOutComplete()
        }
    }
}