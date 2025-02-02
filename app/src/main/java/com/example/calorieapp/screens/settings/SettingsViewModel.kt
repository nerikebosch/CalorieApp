package com.example.calorieapp.screens.settings


import com.example.calorieapp.ACTIVITY_SCREEN
import com.example.calorieapp.GOAL_CHANGE_SCREEN
import com.example.calorieapp.SPLASH_SCREEN
import com.example.calorieapp.USER_CHANGE_SCREEN
import com.example.calorieapp.model.User
import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.LogService
import com.example.calorieapp.model.service.StorageService
import com.example.calorieapp.screens.CalorieAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


/**
 * ViewModel for managing user settings and account actions.
 * Handles user data retrieval, authentication state, and navigation events.
 *
 * @param logService Service for logging app events.
 * @param accountService Service for managing user authentication and account actions.
 * @param storageService Service for managing user data storage.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    logService: LogService,
    private val accountService: AccountService,
    private val storageService: StorageService,
) : CalorieAppViewModel(logService) {

    /**
     * Holds the current user data as a state flow.
     */
    private val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()

    /**
     * Loads the current user data from the account service.
     */
    fun onUserLoad() {
        launchCatching {
            accountService.currentUser.collect { fetchedUser ->
                _user.value = fetchedUser
            }
        }
    }

    init {
        // Collect user data when ViewModel is initialized
        launchCatching {
            println("SettingsVMDebug user authenticated? ${accountService.hasUser}")
            println("SettingsVMDebug: Current user ID: ${accountService.currentUserId}")

            accountService.currentUser.collect { fetchedUser ->
                _user.value = fetchedUser
            }
        }
    }

    /**
     * Signs the user out and restarts the app at the splash screen.
     *
     * @param restartApp Function to restart the app with a specified screen.
     */
    fun onSignOutClick(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.signOut()
            println("SettingsVMDebug: User signed out, navigating to $SPLASH_SCREEN")
            restartApp(SPLASH_SCREEN)
        }
    }

    /**
     * Navigates to the user change screen.
     *
     * @param openScreen Function to navigate to a specified screen.
     */
    fun onUserChangeClick(openScreen: (String) -> Unit) = openScreen(USER_CHANGE_SCREEN)

    /**
     * Navigates to the goal change screen.
     *
     * @param openScreen Function to navigate to a specified screen.
     */
    fun onGoalChangeClick(openScreen: (String) -> Unit) = openScreen(GOAL_CHANGE_SCREEN)

    /**
     * Navigates to the activity tracking screen.
     *
     * @param openScreen Function to navigate to a specified screen.
     */
    fun onOpenActivityClick(openScreen: (String) -> Unit) = openScreen(ACTIVITY_SCREEN)
}