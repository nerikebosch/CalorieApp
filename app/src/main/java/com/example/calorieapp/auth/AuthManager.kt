//package com.example.calorieapp.auth
//
//import android.content.Context
//import androidx.credentials.CredentialManager
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//
//const val WEB_CLIENT_ID = "501150034108-c1b0su0oha3l6bqs0jbgkib8iqfouc7c.apps.googleusercontent.com"
//
//class AuthManager(private val auth: FirebaseAuth) {
//    fun getCurrentUser(): FirebaseUser? {
//        return auth.currentUser
//    }
//
//    fun isUserLoggedIn(): Boolean {
//        return auth.currentUser != null
//    }
//}
//
//
//class AuthManagerSetup(
//    auth: FirebaseAuth,
//    context: Context
//) {
//    val authManager: AuthManager
//    val googleSignInManager: GoogleSignInManager
//    val emailPasswordAuthManager: EmailPasswordAuthManager
//    //val facebookAuthManager: FacebookAuthManager
//
//    init {
//        val credentialManager = CredentialManager.create(context)
//
//        authManager = AuthManager(auth)
//        googleSignInManager = GoogleSignInManager(
//            context = context,
//            auth = auth,
//            credentialManager = credentialManager,
//            webClientId = WEB_CLIENT_ID
//        )
//        emailPasswordAuthManager = EmailPasswordAuthManager(auth)
//        //facebookAuthManager = FacebookAuthManager(auth)
//    }
//}