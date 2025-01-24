package com.example.calorieapp.screens.settings.goalchange


import android.provider.SyncStateContract.Helpers.update
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
            println("GoalChangeVMDebug: Is user authenticated? ${accountService.hasUser}")
            println("GoalChangeVMDebug: Current user ID: ${accountService.currentUserId}")

            accountService.currentUser.collect { fetchedUser ->
                _user.value = fetchedUser
            }
        }
    }

    fun onGoalWaterChange(newValue: Double) {
        _user.value = _user.value.copy(goalWater = newValue)
        println("GoalChangeVMDebug: on user change called, Goal Water: ${_user.value.goalWater}")
    }

    fun onGoalWeightChange(newValue: Double) {
        _user.value = _user.value.copy(goalWeight = newValue)
        println("GoalChangeVMDebug: on user change called, Goal Weight: ${_user.value.goalWeight}")
    }

    fun onGoalCalorieChange(newValue: Double) {
        _user.value = _user.value.copy(goalCalorie = newValue)
        println("GoalChangeVMDebug: on user change called, Goal Calorie: ${_user.value.goalCalorie}")
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


