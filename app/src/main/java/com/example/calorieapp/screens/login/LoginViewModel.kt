package com.example.calorieapp.screens.login

import androidx.compose.runtime.mutableStateOf
import com.example.calorieapp.LOGIN_SCREEN
import com.example.calorieapp.SETTINGS_SCREEN
import com.example.calorieapp.auth.GoogleSignInManager
import com.example.calorieapp.common.ext.isValidEmail
import com.example.calorieapp.common.snackbar.SnackbarManager
import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.LogService
import com.example.calorieapp.screens.CalorieAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.calorieapp.R.string as AppText


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountService: AccountService,
    private val googleSignInManager: GoogleSignInManager,
    logService: LogService
) : CalorieAppViewModel(logService) {

    var uiState = mutableStateOf(LoginUiState())
        private set

    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onSignInClick(openAndPopUp: (String, String) -> Unit) {
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }

        if (password.isBlank()) {
            SnackbarManager.showMessage(AppText.empty_password_error)
            return
        }

        launchCatching {
            accountService.authenticate(email, password)
            //openAndPopUp(HOME_SCREEN, LOGIN_SCREEN)
            openAndPopUp(SETTINGS_SCREEN, LOGIN_SCREEN)
        }
    }

    fun onForgotPasswordClick() {
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }

        launchCatching {
            accountService.sendRecoveryEmail(email)
            SnackbarManager.showMessage(AppText.recovery_email_sent)
        }
    }

    fun onGoogleSignInClick(
        openAndPopUp: (String, String) -> Unit
    ) {
        launchCatching {
            googleSignInManager.initiateGoogleSignIn(
                //scope = viewModelScope,
                onSuccess = {
                    // Navigate to settings screen after successful Google Sign-In
                    openAndPopUp(SETTINGS_SCREEN, LOGIN_SCREEN)
                },
                onError = { exception ->
                    // Handle sign-in error
                    SnackbarManager.showMessage(AppText.generic_error)
                    //
                }
            )
        }
    }

    fun onFacebookSignInClick() {
        // Placeholder for Facebook sign-in implementation
        // You'll need to implement this method similarly to Google Sign-In
        //SnackbarManager.showMessage(AppText.feature_not_implemented)
    }
}