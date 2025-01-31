package com.example.calorieapp.screens.adddata

import com.example.calorieapp.MEAL_TIME_SCREEN
import com.example.calorieapp.model.MealName
import com.example.calorieapp.model.Product
import com.example.calorieapp.model.UserProducts
import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.LogService
import com.example.calorieapp.model.service.StorageService
import com.example.calorieapp.screens.CalorieAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar
import javax.inject.Inject
import java.text.SimpleDateFormat
import java.util.*


/**
 * ViewModel responsible for managing meal time data, including selecting dates,
 * fetching user products, and handling meal product modifications.
 */
@HiltViewModel
class MealTimeViewModel @Inject constructor(
    logService: LogService,
    private val accountService: AccountService,
    private val storageService: StorageService,
) : CalorieAppViewModel(logService) {


    /** StateFlow to hold the user's meal data for a selected date. */
    private val _userProducts = MutableStateFlow<UserProducts?>(null)
    val userProducts: StateFlow<UserProducts?> = _userProducts

    /** StateFlow to hold the currently selected date. */
    private val _selectedDate = MutableStateFlow<String>(formatDateToString(Calendar.getInstance().timeInMillis))
    val selectedDate: StateFlow<String> = _selectedDate

    /** StateFlow to indicate loading state during data operations. */
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    /**
     * Updates the selected date state.
     * @param date The timestamp of the selected date.
     */
    fun setSelectedDate(date: Long)     {
        println("MealTimeVMDebug: setSelectedDate called with date: $date")
        _selectedDate.value = formatDateToString(date)
    }

    /**
     * Loads user product data for the given date.
     * @param date The date in string format (yyyy-MM-dd) to fetch the user's meal data.
     */
    fun loadUserProductsForDate(date: String) {
        launchCatching {
//            storageService.userProducts.collect { products ->
//                _userProducts.value = products
            _userProducts.value = storageService.getUserProductByDate(date)
        }
    }

    /**
     * Handles navigation to the breakfast entry screen.
     * @param openAndPopUp Function to navigate and remove the current screen from the stack.
     */
    fun onBreakfastClick(openAndPopUp: (String, String) -> Unit) {
        println("MealTimeVMDebug: onBreakfastClick called with date: ${_selectedDate.value}")
        openAndPopUp("AddDataScreen/Breakfast/${_selectedDate.value}", MEAL_TIME_SCREEN)
    }

    /** Handles navigation to the lunch entry screen. */
    fun onLunchClick(openAndPopUp: (String, String) -> Unit) {
        openAndPopUp("AddDataScreen/Lunch/${_selectedDate.value}", MEAL_TIME_SCREEN)
    }

    /** Handles navigation to the dinner entry screen. */
    fun onDinnerClick(openAndPopUp: (String, String) -> Unit) {
        openAndPopUp("AddDataScreen/Dinner/${_selectedDate.value}", MEAL_TIME_SCREEN)
    }

    /** Handles navigation to the snack entry screen. */
    fun onSnackClick(openAndPopUp: (String, String) -> Unit) {
        openAndPopUp("AddDataScreen/Snack/${_selectedDate.value}", MEAL_TIME_SCREEN)
    }

    /**
     * Deletes a specific product from the selected meal.
     * @param mealName The meal category (Breakfast, Lunch, Dinner, Snack).
     * @param product The product to be removed.
     */
    fun onDeleteProduct(mealName: MealName, product: Product) {
        launchCatching {
            val currentProducts = _userProducts.value ?: return@launchCatching

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
            storageService.updateUserProduct(updatedUserProducts)
        }
    }

    /**
     * Deletes all products for the selected date.
     */
    fun onDeleteAllProducts() {
        launchCatching {
            val currentProducts = _userProducts.value
            if (currentProducts != null) {
                try {
                    storageService.deleteUserProduct(currentProducts.id)
                    _userProducts.value = null  // Clear the local state after successful deletion
                } catch (e: Exception) {
                    // Handle any potential errors
                    throw e  // This will be caught by launchCatching
                }
            }
        }
    }

}

/**
 * Formats a timestamp into a string representation.
 * @param date The timestamp in milliseconds.
 * @return A formatted string in the format yyyy-MM-dd.
 */
fun formatDateToString(date: Long): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(Date(date))
}