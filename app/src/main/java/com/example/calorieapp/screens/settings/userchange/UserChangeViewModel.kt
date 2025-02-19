package com.example.calorieapp.screens.settings.userchange

import com.example.calorieapp.common.snackbar.SnackbarManager
import com.example.calorieapp.model.User
import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.LogService
import com.example.calorieapp.model.service.StorageService
import com.example.calorieapp.screens.CalorieAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.example.calorieapp.R.string as AppText


/**
 * ViewModel for the User Change screen. Manages the state and business logic for updating user details.
 *
 * @param accountService Service to manage account-related operations such as fetching and linking user data.
 * @param storageService Service to manage persistent storage operations.
 * @param logService Service to log application events and errors.
 */
@HiltViewModel
class UserChangeViewModel @Inject constructor(
    private val accountService: AccountService,
    private val storageService: StorageService,
    logService: LogService,
) : CalorieAppViewModel(logService) {

    private val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()


    init {
        // Collect user data when ViewModel is initialized
        launchCatching {
            println("ChangeScreenDebug: Is user authenticated? ${accountService.hasUser}")
            println("ChangeScreenDebug: Current user ID: ${accountService.currentUserId}")
            accountService.currentUser.collect { fetchedUser ->
                _user.value = fetchedUser
            }
        }
    }

    /**
     * Updates the user's name in the state.
     *
     * @param newValue The new name for the user.
     */
    fun onNameChange(newValue: String) {
        _user.value = _user.value.copy(name = newValue)
    }

    /**
     * Updates the user's surname in the state.
     *
     * @param newValue The new surname for the user.
     */
    fun onSurnameChange(newValue: String) {
        _user.value = _user.value.copy(surname = newValue)
    }

    /**
     * Saves the user's updated data to the account service and navigates back.
     * Validates user input before saving and displays appropriate error messages.
     *
     * @param popUpScreen A lambda function to handle navigation after the operation is completed.
     */
    fun onDoneClick(popUpScreen: () -> Unit) {
        if (_user.value.name.isBlank() ) {
            SnackbarManager.showMessage(AppText.empty_name_error)
            return
        }

        if (_user.value.surname.isBlank() ) {
            SnackbarManager.showMessage(AppText.empty_surname_error)
            return
        }

        if (_user.value.id.isBlank()) {
            launchCatching { throw IllegalStateException("User ID cannot be empty") }
            return
        }

        launchCatching {
            accountService.linkAccount(_user.value)
            popUpScreen()
            SnackbarManager.showMessage(AppText.change_saved)
        }

    }

}


