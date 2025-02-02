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


/**
 * ViewModel responsible for handling goal change operations related to water intake, weight, and calorie intake.
 * It interacts with the AccountService and StorageService to fetch and update user data.
 *
 * @param accountService The service handling user account operations.
 * @param storageService The service handling data storage operations.
 * @param logService The service handling logging operations.
 */
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

    /**
     * Updates the user's goal water intake value.
     * @param newValue The new goal water intake in ml.
     */
    fun onGoalWaterChange(newValue: Double) {
        _user.value = _user.value.copy(goalWater = newValue)
        println("GoalChangeVMDebug: on user change called, Goal Water: ${_user.value.goalWater}")
    }

    /**
     * Updates the user's goal weight value.
     * @param newValue The new goal weight in kg.
     */
    fun onGoalWeightChange(newValue: Double) {
        _user.value = _user.value.copy(goalWeight = newValue)
        println("GoalChangeVMDebug: on user change called, Goal Weight: ${_user.value.goalWeight}")
    }

    /**
     * Updates the user's goal calorie intake value.
     * @param newValue The new daily goal calorie intake in kcal.
     */
    fun onGoalCalorieChange(newValue: Double) {
        _user.value = _user.value.copy(goalCalorie = newValue)
        println("GoalChangeVMDebug: on user change called, Goal Calorie: ${_user.value.goalCalorie}")
    }

    /**
     * Saves the updated goal values to the account service and navigates back.
     * Shows an error message if any goal value is invalid (NaN).
     * @param popUpScreen Function to navigate back after saving.
     */
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


