package com.example.calorieapp.screens.settings


import androidx.lifecycle.viewModelScope
import com.example.calorieapp.ACTIVITY_SCREEN
import com.example.calorieapp.GOAL_CHANGE_SCREEN
import com.example.calorieapp.MAP_SCREEN
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    logService: LogService,
    private val accountService: AccountService,
    private val storageService: StorageService,
) : CalorieAppViewModel(logService) {

    private val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()

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

    fun onSignOutClick(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.signOut()
            println("SettingsVMDebug: User signed out, navigating to $SPLASH_SCREEN")
            restartApp(SPLASH_SCREEN)
        }
    }

    fun onUserChangeClick(openScreen: (String) -> Unit) = openScreen(USER_CHANGE_SCREEN)

    fun onGoalChangeClick(openScreen: (String) -> Unit) = openScreen(GOAL_CHANGE_SCREEN)

    fun onOpenMapScreenClick(openScreen: (String) -> Unit) = openScreen(MAP_SCREEN)

    fun onOpenActivityClick(openScreen: (String) -> Unit) = openScreen(ACTIVITY_SCREEN)
}