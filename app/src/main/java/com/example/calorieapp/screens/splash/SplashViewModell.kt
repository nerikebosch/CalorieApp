package com.example.calorieapp.screens.splash

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.calorieapp.HOME_SCREEN
import com.example.calorieapp.LOGIN_SCREEN
import com.example.calorieapp.SPLASH_SCREEN
import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.ConfigurationService
import com.example.calorieapp.model.service.LogService
import com.example.calorieapp.screens.CalorieAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    configurationService: ConfigurationService,
    private val accountService: AccountService,
    logService: LogService
) : CalorieAppViewModel(logService) {

    private val _showError = mutableStateOf(false)
    val showError = _showError as State<Boolean> // Make it read-only for the UI

    private var isNavigating = false // Prevent multiple navigation attempts

    init {
        launchCatching {
            configurationService.fetchConfiguration()
        }
    }

    fun onAppStart(openAndPopUp: (String, String) -> Unit) {
        if (isNavigating) return  // Prevent multiple navigation attempts

        launchCatching(snackbar = false) {
            isNavigating = true
            _showError.value = false

            try {
                // Navigate based on authentication state
                if (accountService.hasUser) {
                    openAndPopUp(HOME_SCREEN, SPLASH_SCREEN)
                } else {
                    openAndPopUp(LOGIN_SCREEN, SPLASH_SCREEN)
                }
            } catch (e: Exception) {
                _showError.value = true
                isNavigating = false
                throw e
            }
        }
    }
}