package com.example.calorieapp.screens.homescreen

import androidx.compose.runtime.mutableStateOf
import com.example.calorieapp.LOGIN_SCREEN
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
    private val storageService: StorageService
) : CalorieAppViewModel(logService) {

    val options = mutableStateOf<List<String>>(listOf())
    val data = storageService.data

    fun onSettingsClick(openScreen: (String) -> Unit) = openScreen(SETTINGS_SCREEN)

    fun onSignOutClick(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.signOut()
            restartApp(LOGIN_SCREEN)
        }

    }

}