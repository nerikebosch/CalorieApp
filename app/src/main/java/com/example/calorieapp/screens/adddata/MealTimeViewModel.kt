package com.example.calorieapp.screens.adddata

import androidx.lifecycle.viewModelScope
import com.example.calorieapp.model.UserProducts
import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.LogService
import com.example.calorieapp.model.service.StorageService
import com.example.calorieapp.screens.CalorieAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject


@HiltViewModel
class MealTimeViewModel @Inject constructor(
    logService: LogService,
    private val accountService: AccountService,
    private val storageService: StorageService,
) : CalorieAppViewModel(logService) {

    private val _userProducts = MutableStateFlow<UserProducts?>(null)
    val userProducts: StateFlow<UserProducts?> = _userProducts

    private val _selectedDate = MutableStateFlow<Long>(Calendar.getInstance().timeInMillis)
    val selectedDate: StateFlow<Long> = _selectedDate

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

//    fun setSelectedDate(date: Long){
//        println("MealTimeVMDebug: setSelectedDate called with date: $date")
//        _selectedDate.value = date
//        viewModelScope.launch {
//            loadUserProductsForDate()
//        }
//    }

    suspend fun loadUserProductsForDate(date: Long) {
        try {
            _isLoading.value = true
            val userId = accountService.currentUserId
            if (userId.isEmpty()) {
                _userProducts.value = null
                return
            }

            _userProducts.value = storageService.getUserProductByDate(date)
            println("MealTimeVMDebug: Loaded userProducts: ${_userProducts.value}")
        } catch (e: Exception) {
            _userProducts.value = null
        } finally {
            _isLoading.value = false
        }
    }

    fun onBreakfastClick(openAndPopUp: (String, String) -> Unit) {
        openAndPopUp("AddDataScreen/Breakfast", "MealTimeScreen")
    }

    fun onLunchClick(openAndPopUp: (String, String) -> Unit) {
        openAndPopUp("AddDataScreen/Lunch", "MealTimeScreen")
    }

    fun onDinnerClick(openAndPopUp: (String, String) -> Unit) {
        openAndPopUp("AddDataScreen/Dinner", "MealTimeScreen")
    }

    fun onSnackClick(openAndPopUp: (String, String) -> Unit) {
        openAndPopUp("AddDataScreen/Snack", "MealTimeScreen")
    }
}