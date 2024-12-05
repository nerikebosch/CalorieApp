//package com.example.calorieapp.loginscreens
//
//import android.content.Context
//import android.content.Intent
//import android.content.IntentSender
//import com.google.android.gms.auth.api.identity.BeginSignInRequest
//import com.google.android.gms.auth.api.identity.SignInClient
//import com.google.firebase.auth.ktx.auth
//import com.google.firebase.ktx.Firebase
//import kotlinx.coroutines.tasks.await
//import kotlin.coroutines.cancellation.CancellationException
//import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
//import com.example.calorieapp.R
//import com.google.firebase.auth.GoogleAuthProvider
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.callbackFlow
//
//class GoogleAuthUiClient(
//    private val context: Context,
//    private val oneTapClient: SignInClient,
//) {
//    private val auth = Firebase.auth
//
//    suspend fun signIn(): IntentSender? {
//        val result = try {
//            oneTapClient.beginSignIn(
//                buildSignInRequest()
//            ).await()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            if (e is CancellationException) throw e
//            null
//        }
//        return result?.pendingIntent?.intentSender
//    }
//
//    suspend fun signInWithIntent(intent: Intent): SignInResult {
//        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
//        val googleIdToken = credential.googleIdToken
//        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
//
//        return try {
//            val user = auth.signInWithCredential(googleCredentials).await().user
//            SignInResult(
//                data = user?.run {
//                    UserData(
//                        userId = uid,
//                        userName = displayName,
//                        profilePictureUrl = photoUrl?.toString()
//                    )
//                },
//                errorMessage = null
//            )
//        } catch(e: Exception) {
//            e.printStackTrace()
//            if (e is CancellationException) throw e
//            SignInResult(
//                data = null,
//                errorMessage = e.message
//            )
//        }
//    }
//
//    suspend fun signOut() {
//        try {
//            oneTapClient.signOut().await()
//            auth.signOut()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            if (e is CancellationException) throw e
//        }
//    }
//
//    private fun buildSignInRequest(): BeginSignInRequest {
//        return BeginSignInRequest.Builder()
//            .setGoogleIdTokenRequestOptions(
//                GoogleIdTokenRequestOptions.builder()
//                    .setSupported(true)
//                    .setFilterByAuthorizedAccounts(false)
//                    .setServerClientId(context.getString(R.string.google_client_id))
//                    .build()
//
//            )
//            .setAutoSelectEnabled(true)
//            .build()
//    }
//}
//
//class AuthenticationManager {
//
//    private val auth = Firebase.auth
//
//    fun createAccountWithEmail(email: String, password: String): Flow<AuthResponse> = callbackFlow {
//
//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    // Registration successful
//                    trySend(AuthResponse.Success)
//                } else {
//                    // Registration failed
//                    trySend(AuthResponse.Error(errorMessage = task.exception?.message ?: ""))
//                }
//            }
//    }
//
//    fun loginWithEmail(email: String, password: String): Flow<AuthResponse> = callbackFlow {
//        auth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    // Login successful
//                    trySend(AuthResponse.Success)
//                } else {
//                    // Login failed
//                    trySend(AuthResponse.Error(errorMessage = task.exception?.message ?: ""))
//                }
//            }
//    }
//}
//
//interface AuthResponse {
//    data object Success : AuthResponse
//    data class Error(val errorMessage: String) : AuthResponse
//}