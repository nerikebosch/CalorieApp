package com.example.calorieapp.screens.adddata

import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.LogService
import com.example.calorieapp.model.service.StorageService
import com.example.calorieapp.screens.CalorieAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddDataViewModel @Inject constructor(
    logService: LogService,
    private val accountService: AccountService,
    storageService: StorageService
) : CalorieAppViewModel(logService) {

    fun onSaveClick(openAndPopUp: (String, String) -> Unit) = openAndPopUp("MealTimeScreen", "AddDataScreen")
}