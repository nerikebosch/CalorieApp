package com.example.calorieapp.screens.homescreen

import androidx.compose.runtime.mutableStateOf
import com.example.calorieapp.SETTINGS_SCREEN
import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.LogService
import com.example.calorieapp.model.service.StorageService
import com.example.calorieapp.screens.CalorieAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    logService: LogService,
    private val accountService: AccountService,
    private val storageService: StorageService,
) : CalorieAppViewModel(logService) {


    val user = accountService.currentUser

    var uiState = mutableStateOf(HomeScreenUiState())
        private set

    private val goalCalorie
        get() = uiState.value.goalCalorie

    private val currentCalorie
        get() = uiState.value.currentCalorie

    private val currentWeight
        get() = uiState.value.currentWeight

    private val goalWeight
        get() = uiState.value.goalWeight

    private val currentWater
        get() = uiState.value.currentWater

    private val goalWater
        get() = uiState.value.goalWater

    private val mealTitle
        get() = uiState.value.mealTitle

    private val mealCalories
        get() = uiState.value.mealCalories

    private val mealProteins
        get() = uiState.value.mealProteins

    private val mealCarbs
        get() = uiState.value.mealCarbs

    private val mealFats
        get() = uiState.value.mealFats

    private val mealRDC
        get() = uiState.value.mealRDC

    private fun updateUiState(newState: HomeScreenUiState) {
        uiState.value = newState
    }

    fun updateCalories(current: Float, goal: Float) {
        updateUiState(uiState.value.copy(
            currentCalorie = current,
            goalCalorie = goal
        ))
    }

    fun updateWeight(current: Float, goal: Float) {
        updateUiState(uiState.value.copy(
            currentWeight = current,
            goalWeight = goal
        ))
    }

    fun updateWater(current: Float, goal: Float) {
        updateUiState(uiState.value.copy(
            currentWater = current,
            goalWater = goal
        ))
    }

    fun updateMealInfo(
        title: String,
        calories: Int,
        proteins: Int,
        carbs: Int,
        fats: Int,
        rdc: Int
    ) {
        updateUiState(uiState.value.copy(
            mealTitle = title,
            mealCalories = calories,
            mealProteins = proteins,
            mealCarbs = carbs,
            mealFats = fats,
            mealRDC = rdc
        ))
    }

    val options = mutableStateOf<List<String>>(listOf())

    val userData = storageService.userData

    fun onSettingsClick(openScreen: (String) -> Unit) = openScreen(SETTINGS_SCREEN)

}