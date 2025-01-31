package com.example.calorieapp.screens.sign_up

import androidx.compose.runtime.mutableStateOf
import com.example.calorieapp.LOGIN_SCREEN
import com.example.calorieapp.MORE_ABOUT_YOU_SCREEN
import com.example.calorieapp.SIGN_UP_SCREEN
import com.example.calorieapp.SPLASH_SCREEN
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

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService
) : CalorieAppViewModel(logService) {

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

    fun onNameChange(newValue: String) {
        uiState.value = uiState.value.copy(name = newValue)
    }

    fun onSurnameChange(newValue: String) {
        uiState.value = uiState.value.copy(surname = newValue)
    }

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

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

    fun onLoginScreenClick(openAndPopUp: (String, String) -> Unit) = openAndPopUp(LOGIN_SCREEN, SIGN_UP_SCREEN)
}