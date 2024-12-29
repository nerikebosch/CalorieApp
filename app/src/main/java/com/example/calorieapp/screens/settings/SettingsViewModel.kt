package com.example.calorieapp.screens.settings


import com.example.calorieapp.SETTINGS_SCREEN
import com.example.calorieapp.SPLASH_SCREEN
import com.example.calorieapp.USER_CHANGE_SCREEN
import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.LogService
import com.example.calorieapp.model.service.StorageService
import com.example.calorieapp.screens.CalorieAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    logService: LogService,
    private val accountService: AccountService,
    private val storageService: StorageService
) : CalorieAppViewModel(logService) {

    val uiState = accountService.currentUser.map { SettingsUiState(it.registeredUser) }

    fun onSignOutClick(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.signOut()
            restartApp(SPLASH_SCREEN)
        }
    }

    fun onUserChangeClick(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            openAndPopUp(USER_CHANGE_SCREEN, SETTINGS_SCREEN)
        }

    }

//    fun onDeleteMyAccountClick(restartApp: (String) -> Unit) {
//        launchCatching {
//            storageService.deleteAllForUser(accountService.currentUserId)
//            accountService.deleteAccount()
//            restartApp(SPLASH_SCREEN)
//        }
//    }
}