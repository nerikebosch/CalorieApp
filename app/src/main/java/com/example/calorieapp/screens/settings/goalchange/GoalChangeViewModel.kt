package com.example.calorieapp.screens.settings.goalchange


import androidx.lifecycle.viewModelScope
import com.example.calorieapp.common.snackbar.SnackbarManager
import com.example.calorieapp.model.User
import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.LogService
import com.example.calorieapp.model.service.StorageService
import com.example.calorieapp.screens.CalorieAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.calorieapp.R.string as AppText

@HiltViewModel
class GoalChangeViewModel @Inject constructor(
    private val accountService: AccountService,
    private val storageService: StorageService,
    logService: LogService,
) : CalorieAppViewModel(logService) {

    private val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()

    init {
        // Collect user data when ViewModel is initialized
        launchCatching {
            println("GoalChangeScreenDebug: Is user authenticated? ${accountService.hasUser}")
            println("GoalChangeScreenDebug: Current user ID: ${accountService.currentUserId}")

            accountService.currentUser.collect { fetchedUser ->
                _user.value = fetchedUser
            }
        }
    }

    fun onUserChange(update: User) {
        _user.value = update
    }

    fun onDoneClick(popUpScreen: () -> Unit) {
        if (_user.value.goalWater.isNaN() || _user.value.goalWeight.isNaN() || _user.value.goalCalorie.isNaN()) {
            SnackbarManager.showMessage(AppText.goal_error)
            return
        }

        launchCatching {
            accountService.linkAccount(_user.value)
            popUpScreen()
            SnackbarManager.showMessage(AppText.change_saved)
        }

    }

}


