//package com.example.calorieapp.auth
//
//import com.facebook.CallbackManager
//import com.facebook.FacebookCallback
//import com.facebook.FacebookException
//import com.facebook.login.LoginManager
//import com.facebook.login.LoginResult
//import com.google.firebase.auth.FacebookAuthProvider
//import com.google.firebase.auth.FirebaseAuth
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.launch
//
//class FacebookAuthManager(
//    private val auth: FirebaseAuth
//) {
//    private val callbackManager = CallbackManager.Factory.create()
//    private val loginManager = LoginManager.getInstance()
//
//    fun signIn(
//        scope: CoroutineScope,
//        onSuccess: () -> Unit,
//        onError: (Exception) -> Unit
//    ) {
//        loginManager.registerCallback(
//            callbackManager,
//            object : FacebookCallback<LoginResult> {
//                override fun onSuccess(result: LoginResult) {
//                    val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
//                    auth.signInWithCredential(credential)
//                        .addOnCompleteListener { task ->
//                            if (task.isSuccessful) {
//                                onSuccess()
//                            } else {
//                                onError(task.exception ?: Exception("Facebook sign-in failed"))
//                            }
//                        }
//                }
//
//                override fun onCancel() {
//                    onError(Exception("Facebook sign-in cancelled"))
//                }
//
//                override fun onError(error: FacebookException) {
//                    onError(error)
//                }
//            }
//        )
//
//        // Trigger Facebook login
//        scope.launch {
//            loginManager.logInWithReadPermissions(
//                null, // You'll need to pass the appropriate activity/fragment
//                listOf("email", "public_profile")
//            )
//        }
//    }
//
//    fun signOut() {
//        auth.signOut()
//        loginManager.logOut()
//    }
//}