package com.example.calorieapp.screens.homescreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.calorieapp.SETTINGS_SCREEN
import com.example.calorieapp.model.MealName
import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.LogService
import com.example.calorieapp.model.service.NutritionService
import com.example.calorieapp.model.service.StorageService
import com.example.calorieapp.screens.CalorieAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    logService: LogService,
    private val accountService: AccountService,
    private val storageService: StorageService,
    private val nutritionService: NutritionService
) : CalorieAppViewModel(logService) {

    val user = accountService.currentUser

    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            val currentMealName = getCurrentMealName(currentHour)

            storageService.userProducts.collect { products ->
                val currentCalorie = nutritionService.getTotalCaloriesForDate(products, today)
                val todayProducts = products.find { it.date == today }

                todayProducts?.let { userProducts ->
                    val mealNutrients = nutritionService.getMealDataByType(userProducts, currentMealName)
                    _uiState.value = HomeScreenUiState(
                        currentCalorie = currentCalorie,
                        mealTitle = currentMealName.name,
                        mealCalories = mealNutrients.totalCalories,
                        mealProteins = mealNutrients.totalProtein,
                        mealCarbs = mealNutrients.totalCarbohydrates,
                        mealFats = mealNutrients.totalFat
                    )
                }
            }
        }
    }

    val options = mutableStateOf<List<String>>(listOf())


    fun onSettingsClick(openScreen: (String) -> Unit) = openScreen(SETTINGS_SCREEN)

    private fun getCurrentMealName(hour: Int): MealName {
        return when (hour) {
            in 5..10 -> MealName.Breakfast
            in 11..14 -> MealName.Lunch
            in 15..20 -> MealName.Dinner
            else -> MealName.Snack
        }
    }
}