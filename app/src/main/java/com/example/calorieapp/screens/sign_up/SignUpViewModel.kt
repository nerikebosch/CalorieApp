package com.example.calorieapp.screens.sign_up

import androidx.compose.runtime.mutableStateOf
import com.example.calorieapp.LOGIN_SCREEN
import com.example.calorieapp.MORE_ABOUT_YOU_SCREEN
import com.example.calorieapp.SIGN_UP_SCREEN
import com.example.calorieapp.common.ext.isValidEmail
import com.example.calorieapp.common.ext.isValidPassword
import com.example.calorieapp.common.snackbar.SnackbarManager
import com.example.calorieapp.model.User
import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.LogService
import com.example.calorieapp.screens.CalorieAppViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.example.calorieapp.R.string as AppText


/**
 * ViewModel for handling sign-up logic and UI state management.
 *
 * @property accountService Service for managing user accounts.
 * @property logService Service for logging errors and events.
 */
@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService
) : CalorieAppViewModel(logService) {

    /** Mutable state representing the UI state of the sign-up screen. */
    var uiState = mutableStateOf(SignUpUiState())
        private set

    private val name
        get() = uiState.value.name
    private val surname
        get() = uiState.value.surname
    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password

    /**
     * Updates the name field in the UI state.
     *
     * @param newValue The new name value.
     */
    fun onNameChange(newValue: String) {
        uiState.value = uiState.value.copy(name = newValue)
    }

    /**
     * Updates the surname field in the UI state.
     *
     * @param newValue The new surname value.
     */
    fun onSurnameChange(newValue: String) {
        uiState.value = uiState.value.copy(surname = newValue)
    }

    /**
     * Updates the email field in the UI state.
     *
     * @param newValue The new email value.
     */
    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    /**
     * Updates the password field in the UI state.
     *
     * @param newValue The new password value.
     */
    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    /**
     * Handles the sign-up process. Validates input fields and creates a new user account.
     *
     * @param openAndPopUp Function to navigate to the next screen and remove the current one.
     */
    fun onSignUpClick(openAndPopUp: (String, String) -> Unit) {
        if (name.isBlank() || surname.isBlank() || email.isBlank() || password.isBlank()) {
            SnackbarManager.showMessage(AppText.all_fields_required)
            return
        }

        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }

        if (!password.isValidPassword()) {
            SnackbarManager.showMessage(AppText.password_error)
            return
        }



        launchCatching {
            val authResult = FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .await()

            // Get the current user
            val firebaseUser = authResult.user ?: throw Exception("User creation failed")

            val user = User(
                id = firebaseUser.uid,
                name = uiState.value.name,
                surname = uiState.value.surname,
                registeredUser = true,
                email = uiState.value.email
            )

            accountService.linkAccount(user)
            openAndPopUp(MORE_ABOUT_YOU_SCREEN, SIGN_UP_SCREEN)
        }
    }

    /**
     * Navigates to the login screen.
     *
     * @param openAndPopUp Function to navigate to the next screen and remove the current one.
     */
    fun onLoginScreenClick(openAndPopUp: (String, String) -> Unit) = openAndPopUp(LOGIN_SCREEN, SIGN_UP_SCREEN)
}