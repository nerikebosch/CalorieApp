package com.example.calorieapp.model.service.impl

import com.example.calorieapp.model.User
import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.trace
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AccountService {

    private val firestore = FirebaseFirestore.getInstance()

    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    override val hasUser: Boolean
        get() = auth.currentUser != null

    override val currentUser: Flow<User>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    this.trySend(auth.currentUser?.let {
                        User(
                            it.uid
                        ) } ?: User())
                }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }
//    override val currentUser: Flow<User>
//    get() = callbackFlow {
//        val listener = FirebaseAuth.AuthStateListener { auth ->
//            val firebaseUser = auth.currentUser
//            if (firebaseUser != null) {
//                // Get user data from Firestore
//                firestore.collection("users")
//                    .document(firebaseUser.uid)
//                    .get()
//                    .addOnSuccessListener { document ->
//                        if (document != null && document.exists()) {
//                            val user = User(
//                                id = firebaseUser.uid,
//                                name = document.getString("name").orEmpty(),
//                                surname = document.getString("surname").orEmpty(),
//                                email = document.getString("email").orEmpty(),
//                                registeredUser = document.getBoolean("registeredUser") ?: true
//                            )
//                            trySend(user)
//                        } else {
//                            trySend(User())
//                        }
//                    }
//                    .addOnFailureListener {
//                        trySend(User())
//                    }
//            } else {
//                trySend(User())
//            }
//        }
//        auth.addAuthStateListener(listener)
//        awaitClose { auth.removeAuthStateListener(listener) }
//    }

    override suspend fun authenticate(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun sendRecoveryEmail(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    override suspend fun createAnonymousAccount() {
        auth.signInAnonymously().await()
    }

    override suspend fun linkAccount(email: String, password: String): Unit =
        trace(LINK_ACCOUNT_TRACE) {
            val credential = EmailAuthProvider.getCredential(email, password)
            auth.currentUser!!.linkWithCredential(credential).await()
        }
//
//    override suspend fun deleteAccount() {
//        auth.currentUser!!.delete().await()
//    }

    override suspend fun signOut() {
        if (auth.currentUser!!.isAnonymous) {
            auth.currentUser!!.delete()
        }
        auth.signOut()

        // Sign the user back in anonymously.
        createAnonymousAccount()
    }

    override suspend fun signInWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).await()
    }


    companion object {
        private const val LINK_ACCOUNT_TRACE = "linkAccount"
    }
}
