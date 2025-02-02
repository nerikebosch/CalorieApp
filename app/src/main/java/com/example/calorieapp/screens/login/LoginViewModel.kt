package com.example.calorieapp.screens.login

import androidx.compose.runtime.mutableStateOf
import com.example.calorieapp.LOGIN_SCREEN
import com.example.calorieapp.SIGN_UP_SCREEN
import com.example.calorieapp.SPLASH_SCREEN
import com.example.calorieapp.auth.GoogleSignInManager
import com.example.calorieapp.common.ext.isValidEmail
import com.example.calorieapp.common.snackbar.SnackbarManager
import com.example.calorieapp.common.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.LogService
import com.example.calorieapp.screens.CalorieAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.calorieapp.R.string as AppText


/**
 * ViewModel responsible for handling user authentication logic for the login screen.
 * Manages email/password sign-in, Google sign-in, password recovery, and navigation.
 *
 * @property accountService Service handling user authentication.
 * @property googleSignInManager Manager for handling Google Sign-In.
 * @property logService Service for logging errors.
 */
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

    /**
     * Updates the email field in the UI state.
     *
     * @param newValue The new email entered by the user.
     */
    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    /**
     * Updates the password field in the UI state.
     *
     * @param newValue The new password entered by the user.
     */
    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    /**
     * Attempts to sign in the user with the provided email and password.
     * Displays appropriate error messages if validation fails.
     *
     * @param openAndPopUp Function to navigate to another screen upon successful sign-in.
     */
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
            openAndPopUp(SPLASH_SCREEN, LOGIN_SCREEN)
            //openAndPopUp(SETTINGS_SCREEN, LOGIN_SCREEN)
        }
    }

    /**
     * Sends a password recovery email if the email is valid.
     * Displays an error message if the email is invalid.
     */
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

    /**
     * Initiates the Google Sign-In process.
     * On success, navigates the user to the next screen.
     *
     * @param openAndPopUp Function to handle navigation after successful authentication.
     */
    fun onGoogleSignInClick(
        openAndPopUp: (String, String) -> Unit
    ) {
        launchCatching {
            googleSignInManager.initiateGoogleSignIn(
                //scope = viewModelScope,
                onSuccess = {
                    // Navigate to screen after successful Google Sign-In

                    openAndPopUp(SPLASH_SCREEN, LOGIN_SCREEN)
                },
                onError = { exception ->
                    // Handle sign-in error
                    SnackbarManager.showMessage(AppText.generic_error)
                    SnackbarManager.showMessage(exception.toSnackbarMessage())
                    exception.printStackTrace()
                }
            )
        }
    }

    /**
     * Placeholder function for Facebook Sign-In functionality.
     * Currently shows an error message as Facebook sign-in is not implemented.
     */
    fun onFacebookSignInClick() {
        // Placeholder for Facebook sign-in implementation
        // You'll need to implement this method similarly to Google Sign-In
        SnackbarManager.showMessage(AppText.generic_error)
    }

    /**
     * Navigates the user to the sign-up screen.
     *
     * @param openAndPopUp Function to handle navigation.
     */
    fun onSignUpScreenClick(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            openAndPopUp(SIGN_UP_SCREEN, LOGIN_SCREEN)
        }
    }
}