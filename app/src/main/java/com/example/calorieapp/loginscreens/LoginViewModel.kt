package com.example.calorieapp.loginscreens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calorieapp.auth.AuthRepository
import com.example.calorieapp.auth.GoogleSignInManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService,
) : CalorieAppViewModel(logService), CoroutineScope {() {
    private val _loginResult = MutableStateFlow<AuthResult?>(null)
    val loginResult = _loginResult.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepository.loginWithEmailPassword(email, password).collect { result ->
                _loginResult.value = result
            }
        }
    }

    fun googleSignIn() {
        viewModelScope.launch {
            googleSignInManager.initiateGoogleSignIn(viewModelScope).collect { result ->
                _loginResult.value = result
            }
        }
    }

    // Future method for Facebook Sign-In
    fun facebookSignIn() {
        // TODO: Implement Facebook Sign-In logic
    }
}