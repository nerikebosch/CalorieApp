package com.example.calorieapp.screens.adddata

import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.LogService
import com.example.calorieapp.model.service.StorageService
import com.example.calorieapp.screens.CalorieAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MealTimeViewModel @Inject constructor(
    logService: LogService,
    private val accountService: AccountService,
    storageService: StorageService,
) : CalorieAppViewModel(logService) {

    val userData = storageService.userData

    fun onBreakfastClick(openAndPopUp: (String, String) -> Unit) {
        openAndPopUp("AddDataScreen/Breakfast", "MealTimeScreen")
    }

    fun onLunchClick(openAndPopUp: (String, String) -> Unit) {
        openAndPopUp("AddDataScreen/Lunch", "MealTimeScreen")
    }

    fun onDinnerClick(openAndPopUp: (String, String) -> Unit) {
        openAndPopUp("AddDataScreen/Dinner", "MealTimeScreen")
    }

    fun onSnackClick(openAndPopUp: (String, String) -> Unit) {
        openAndPopUp("AddDataScreen/Snack", "MealTimeScreen")
    }
}