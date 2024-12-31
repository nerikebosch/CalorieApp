package com.example.calorieapp.screens.settings.userchange

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
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
class UserChangeViewModel @Inject constructor(
    private val accountService: AccountService,
    private val storageService: StorageService,
    logService: LogService
) : CalorieAppViewModel(logService) {

    private val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun loadUserData() {
        // Collect user data when ViewModel is initialized
        viewModelScope.launch {
            println("ChangeScreenDebug: Is user authenticated? ${accountService.hasUser}")
            println("ChangeScreenDebug: Current user ID: ${accountService.currentUserId}")

            _isLoading.value = true
            accountService.currentUser.collect { fetchedUser ->
                _user.value = fetchedUser
                _isLoading.value = false
            }
        }
    }

    fun onNameChange(newValue: String) {
        _user.value = _user.value.copy(name = newValue)
    }

    fun onSurnameChange(newValue: String) {
        _user.value = _user.value.copy(surname = newValue)
    }

    fun onDoneClick(popUpScreen: () -> Unit) {
        if (_user.value.id.isBlank()) {
            launchCatching { throw IllegalStateException("User ID cannot be empty") }
            return
        }

        launchCatching {
            _isLoading.value = true
            accountService.linkAccount(_user.value)
            _isLoading.value = false
            popUpScreen()
        }

    }
}