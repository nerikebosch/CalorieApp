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
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * ViewModel for the Splash screen. Responsible for managing the app's navigation
 * from the Splash screen to either the Home or Login screen based on the user's authentication state.
 *
 * @property configurationService The service used to fetch app configurations.
 * @property accountService The service used to handle user account information and authentication.
 * @property logService The service used for logging errors and events.
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    configurationService: ConfigurationService,
    private val accountService: AccountService,
    logService: LogService
) : CalorieAppViewModel(logService) {

    private val _showError = mutableStateOf(false)
    val showError = _showError as State<Boolean> // Make it read-only for the UI


    init {
        launchCatching {
            configurationService.fetchConfiguration()
        }
    }

    /**
     * Handles the app's startup logic and navigates the user to the appropriate screen.
     *
     * @param openAndPopUp A lambda function to navigate to a new screen and add the previous screen from the back stack.
     */
    fun onAppStart(openAndPopUp: (String, String) -> Unit) {

        launchCatching {
            _showError.value = false

            try {
                // Add small delay to ensure Firebase auth state is current
                delay(100)
                println("SplashVMDebug: Checking auth state...")
                println("SplashVMDebug: hasUser = ${accountService.hasUser}")

                if (accountService.hasUser && (accountService.currentUserId).isNotEmpty() ) {
                    println("SplashVMDebug: User authenticated, navigating to $HOME_SCREEN")
                    openAndPopUp(HOME_SCREEN, SPLASH_SCREEN)
                } else {
                    println("SplashVMDebug: User not authenticated, navigating to $LOGIN_SCREEN")
                    openAndPopUp(LOGIN_SCREEN, SPLASH_SCREEN)
                }
            } catch (e: Exception) {
                println("SplashVMDebug: Error during navigation: ${e.message}")
                _showError.value = true
                throw e
            }
        }
    }
}