package com.example.calorieapp.screens.information

import com.example.calorieapp.HOME_SCREEN
import com.example.calorieapp.R
import com.example.calorieapp.common.snackbar.SnackbarManager
import com.example.calorieapp.model.User
import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.LogService
import com.example.calorieapp.screens.CalorieAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MoreAboutYouViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService
) : CalorieAppViewModel(logService) {

    private val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()

    init {
        // Collect user data when ViewModel is initialized
        launchCatching {
            println("MoreAboutYouVMDebug: Is user authenticated? ${accountService.hasUser}")
            println("MoreAboutYouVMDebug: Current user ID: ${accountService.currentUserId}")

            accountService.currentUser.collect { fetchedUser ->
                _user.value = fetchedUser
            }
        }
    }

    fun onWeightChange(newValue: Double) {
        _user.value = _user.value.copy(weight = newValue)
    }

    fun onGoalWeightChange(newValue: Double) {
        _user.value = _user.value.copy(goalWeight = newValue)
        println("GoalChangeVMDebug: on user change called, Goal Weight: ${_user.value.goalWeight}")
    }

    fun onHeightChange(newValue: Double) {
        _user.value = _user.value.copy(height = newValue)
    }

    fun onGenderChange(newValue: String) {
        _user.value = _user.value.copy(gender = newValue)
    }

    fun onDobChange(newValue: String) {
        _user.value = _user.value.copy(dob = newValue)
        println("Debug: Date of Birth updated to ${_user.value.dob}")
    }

    fun onAgeChange(newValue: Int) {
        _user.value = _user.value.copy(age = newValue)
        println("Debug: Age updated to ${_user.value.age}")
    }

    fun onGoalWaterChange(newValue: Double) {
        _user.value = _user.value.copy(goalWater = newValue)
    }

    fun calculateGoalCalories() {
        val weight = _user.value.weight
        val height = _user.value.height
        val age = _user.value.age
        val gender = _user.value.gender
        val goalWeight = _user.value.goalWeight

        // Default to Sedentary activity level
        val activityFactor = 1.2

        // Calculate BMR
        val bmr = if (gender.equals("Male", ignoreCase = true)) {
            (10 * weight) + (6.25 * height) - (5 * age) + 5
        } else {
            (10 * weight) + (6.25 * height) - (5 * age) - 161
        }

        val tdee = bmr * activityFactor

        val goalCalorie = when {
            goalWeight > weight -> (tdee + 500) // Weight Gain
            goalWeight < weight -> (tdee - 500) // Weight Loss
            else -> tdee // Maintenance
        }

        // Save the goalCalories in the user state
        _user.value = _user.value.copy(goalCalorie = goalCalorie)

        println("Calorie Calculation Debug: Goal Calories set to $goalCalorie")
    }


    fun onContinueClick(openScreen: (String) -> Unit) {
        calculateGoalCalories()

        openScreen(HOME_SCREEN)

        launchCatching {
            accountService.linkAccount(_user.value)
            SnackbarManager.showMessage(R.string.change_saved)
        }
    }

}