package com.example.calorieapp.auth

import com.google.firebase.auth.FirebaseAuth

class EmailPasswordAuthManager(private val auth: FirebaseAuth) {

    fun signIn(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(task.exception ?: Exception("Unknown error"))
                }
            }
    }

//    fun signUp(
//        email: String,
//        password: String,
//        onSuccess: () -> Unit,
//        onError: (Exception) -> Unit
//    ) {
//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    onSuccess()
//                } else {
//                    onError(task.exception ?: Exception("Unknown error"))
//                }
//            }
//    }

    fun resetPassword(
        email: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(task.exception ?: Exception("Unknown error"))
                }
            }
    }
}