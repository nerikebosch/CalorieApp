package com.example.calorieapp.screens.sign_up

import androidx.compose.runtime.mutableStateOf
import com.example.calorieapp.SETTINGS_SCREEN
import com.example.calorieapp.SIGN_UP_SCREEN
import com.example.calorieapp.common.ext.isValidEmail
import com.example.calorieapp.common.ext.isValidPassword
import com.example.calorieapp.common.snackbar.SnackbarManager
import com.example.calorieapp.model.User
import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.LogService
import com.example.calorieapp.screens.CalorieAppViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
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
                name = name,
                surname = surname,
                registeredUser = true,
                email = email
            )

            FirebaseFirestore.getInstance()
                .collection("users")
                .document(user.id)
                .set(user, SetOptions.merge())
                .await()

            accountService.linkAccount(email, password)
            openAndPopUp(SETTINGS_SCREEN, SIGN_UP_SCREEN)
        }
    }
}