//package com.example.calorieapp.loginscreens
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.calorieapp.auth.AuthRepository
//import com.example.calorieapp.auth.GoogleSignInManager
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//
//class LoginViewModel(
//    private val authRepository: AuthRepository,
//    private val googleSignInManager: GoogleSignInManager
//) : ViewModel() {
//    private val _loginResult = MutableStateFlow<AuthResult?>(null)
//    val loginResult = _loginResult.asStateFlow()
//
//    fun login(email: String, password: String) {
//        viewModelScope.launch {
//            authRepository.loginWithEmailPassword(email, password).collect { result ->
//                _loginResult.value = result
//            }
//        }
//    }
//
//    fun googleSignIn() {
//        viewModelScope.launch {
//            googleSignInManager.initiateGoogleSignIn(viewModelScope).collect { result ->
//                _loginResult.value = result
//            }
//        }
//    }
//
//    // Future method for Facebook Sign-In
//    fun facebookSignIn() {
//        // TODO: Implement Facebook Sign-In logic
//    }
//}