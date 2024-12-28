package com.example.calorieapp.model.service.impl

import com.example.calorieapp.model.User
import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.trace
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AccountService {

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

    override val currentUserObj: Flow<User>
        get() = callbackFlow {
            val listener = FirebaseAuth.AuthStateListener { auth ->
                auth.currentUser?.let { firebaseUser ->
                    firestore.collection("users")
                        .document(firebaseUser.uid)
                        .get()
                        .addOnSuccessListener { document ->
                            val user = document.toObject(User::class.java) ?: User(
                                id = firebaseUser.uid,
                                email = firebaseUser.email ?: "",
                                name = firebaseUser.displayName?.split(" ")?.firstOrNull() ?: "",
                                surname = firebaseUser.displayName?.split(" ")?.lastOrNull() ?: "",
                                registeredUser = document.exists()
                            )
                            trySend(user)
                        }
                } ?: trySend(User()) // Send empty user if not authenticated
            }

            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }

    override suspend fun authenticate(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun sendRecoveryEmail(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    override suspend fun linkAccount(user: User): Unit =
        trace(LINK_ACCOUNT_TRACE) {
            firestore.collection("users")
                .document(user.id)
                .set(user, SetOptions.merge())
                .await()
        }

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun signInWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).await()
    }


    companion object {
        private const val LINK_ACCOUNT_TRACE = "linkAccount"
    }
}
