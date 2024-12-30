package com.example.calorieapp.screens.settings.userchange

import androidx.lifecycle.viewModelScope
import com.example.calorieapp.model.User
import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.LogService
import com.example.calorieapp.model.service.StorageService
import com.example.calorieapp.screens.CalorieAppViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserChangeViewModel @Inject constructor(
    private val accountService: AccountService,
    private val storageService: StorageService,
    logService: LogService
) : CalorieAppViewModel(logService) {

    private val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()

    init {
        // Collect user data when ViewModel is initialized
        viewModelScope.launch {
            println("Debug: Is user authenticated? ${accountService.hasUser}")
            println("Debug: Current user ID: ${accountService.currentUserId}")

            storageService.user.collect { fetchedUser ->
                _user.value = fetchedUser
            }
        }
    }


//    fun onNameChange(newValue: String) {
//        user.name = user.name.copy(title = newValue)
//    }

    fun onDoneClick(popUpScreen: () -> Unit) {
        launchCatching {
            popUpScreen()
        }

    }
}