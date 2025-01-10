package com.example.calorieapp.screens.adddata

import androidx.lifecycle.viewModelScope
import com.example.calorieapp.MEAL_TIME_SCREEN
import com.example.calorieapp.model.MealName
import com.example.calorieapp.model.Product
import com.example.calorieapp.model.User
import com.example.calorieapp.model.UserProducts
import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.LogService
import com.example.calorieapp.model.service.StorageService
import com.example.calorieapp.screens.CalorieAppViewModel
import com.google.android.play.integrity.internal.n
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject
import java.text.SimpleDateFormat
import java.util.*


@HiltViewModel
class MealTimeViewModel @Inject constructor(
    logService: LogService,
    private val accountService: AccountService,
    private val storageService: StorageService,
) : CalorieAppViewModel(logService) {

    // Single UserProducts object state
    private val _userProducts = MutableStateFlow<UserProducts?>(null)
    val userProducts: StateFlow<UserProducts?> = _userProducts


    private val _selectedDate = MutableStateFlow<String>(formatDateToString(Calendar.getInstance().timeInMillis))
    val selectedDate: StateFlow<String> = _selectedDate

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun setSelectedDate(date: Long)     {
        println("MealTimeVMDebug: setSelectedDate called with date: $date")
        _selectedDate.value = formatDateToString(date)
    }

    fun loadUserProductsForDate(date: String) {
        launchCatching {
//            storageService.userProducts.collect { products ->
//                _userProducts.value = products
            _userProducts.value = storageService.getUserProductByDate(date)
        }
    }

    fun onBreakfastClick(openAndPopUp: (String, String) -> Unit) {
        println("MealTimeVMDebug: onBreakfastClick called with date: ${_selectedDate.value}")
        openAndPopUp("AddDataScreen/Breakfast/${_selectedDate.value}", MEAL_TIME_SCREEN)
    }

    fun onLunchClick(openAndPopUp: (String, String) -> Unit) {
        openAndPopUp("AddDataScreen/Lunch/${_selectedDate.value}", MEAL_TIME_SCREEN)
    }

    fun onDinnerClick(openAndPopUp: (String, String) -> Unit) {
        openAndPopUp("AddDataScreen/Dinner/${_selectedDate.value}", MEAL_TIME_SCREEN)
    }

    fun onSnackClick(openAndPopUp: (String, String) -> Unit) {
        openAndPopUp("AddDataScreen/Snack/${_selectedDate.value}", MEAL_TIME_SCREEN)
    }

    fun onDeleteProduct(mealName: MealName, product: Product) {
        viewModelScope.launch {
            val currentProducts = _userProducts.value ?: return@launch

            // Remove product from the correct meal category
            val updatedMealData = when (mealName) {
                MealName.Breakfast -> currentProducts.breakfast.copy(
                    products = currentProducts.breakfast.products.filterNot { it == product }
                )
                MealName.Lunch -> currentProducts.lunch.copy(
                    products = currentProducts.lunch.products.filterNot { it == product }
                )
                MealName.Dinner -> currentProducts.dinner.copy(
                    products = currentProducts.dinner.products.filterNot { it == product }
                )
                MealName.Snack -> currentProducts.snacks.copy(
                    products = currentProducts.snacks.products.filterNot { it == product }
                )
            }

            val updatedUserProducts = currentProducts.copy(
                breakfast = if (mealName == MealName.Breakfast) updatedMealData else currentProducts.breakfast,
                lunch = if (mealName == MealName.Lunch) updatedMealData else currentProducts.lunch,
                dinner = if (mealName == MealName.Dinner) updatedMealData else currentProducts.dinner,
                snacks = if (mealName == MealName.Snack) updatedMealData else currentProducts.snacks
            )

            _userProducts.value = updatedUserProducts


        }
    }


}

fun formatDateToString(date: Long): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(Date(date))
}