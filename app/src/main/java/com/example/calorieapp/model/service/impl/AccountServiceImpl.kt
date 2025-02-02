package com.example.calorieapp.model.service.impl

import android.R.attr.name
import com.example.calorieapp.model.User
import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.trace
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
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
            val listener = FirebaseAuth.AuthStateListener { auth ->
                auth.currentUser?.let { firebaseUser ->
                    firestore.collection("users")
                        .document(firebaseUser.uid)
                        .get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                val user = document.toObject(User::class.java)?.copy(id = firebaseUser.uid)
                                println("AccountServiceDebug: User retrieved from Firestore: $user")
                                trySend(user ?: User())
                            } else {
                                trySend(User()) // Send an empty User if the document doesn't exist
                            }
                        }
                        .addOnFailureListener { exception ->
                            exception.printStackTrace()
                            trySend(User())
                        }
                } ?: trySend(User())
            }

            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }.distinctUntilChanged()


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
        firestore.clearPersistence()
    }

    override suspend fun signInWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val authResult = auth.signInWithCredential(credential).await()

        authResult.user?.let { firebaseUser ->
            val fullName = firebaseUser.displayName?.trim()?.split("\\s+".toRegex(), limit = 2) ?: listOf()
//            val userFromGoogle = User(
//                name = fullName.getOrNull(0) ?: "",
//                surname = fullName.getOrNull(1) ?: "",
//                email = firebaseUser.email ?: "",
//                registeredUser = true
//            )
//            val userData = hashMapOf(
//                "id" to firebaseUser.uid,
//                "name" to (fullName.getOrNull(0) ?: ""),
//                "surname" to (fullName.getOrNull(1) ?: ""),
//                "email" to (firebaseUser.email ?: ""),
//                "registeredUser" to true
//            )
            // Merge with existing Firestore data (if any)
            firestore.collection("users")
                .document(firebaseUser.uid)
                .get()
                .await()
        }
    }


    companion object {
        private const val LINK_ACCOUNT_TRACE = "linkAccount"
    }

}
