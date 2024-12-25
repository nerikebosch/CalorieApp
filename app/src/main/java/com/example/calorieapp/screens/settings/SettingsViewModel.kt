package com.example.calorieapp.screens.settings


import com.example.calorieapp.HOME_SCREEN
import com.example.calorieapp.LOGIN_SCREEN
import com.example.calorieapp.SIGN_UP_SCREEN
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

    fun onLoginClick(openScreen: (String) -> Unit) = openScreen(LOGIN_SCREEN)

    fun onSignUpClick(openScreen: (String) -> Unit) = openScreen(SIGN_UP_SCREEN)

    fun onSignOutClick(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.signOut()
            restartApp(HOME_SCREEN   )
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