package com.example.calorieapp.screens.homescreen

import com.example.calorieapp.SETTINGS_SCREEN
import com.example.calorieapp.model.MealName
import com.example.calorieapp.model.User
import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.LogService
import com.example.calorieapp.model.service.NutritionService
import com.example.calorieapp.model.service.StorageService
import com.example.calorieapp.screens.CalorieAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()

    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    init {
        launchCatching {
            accountService.currentUser.collect { fetchedUser ->
                _user.value = fetchedUser

                println("HomeVMDebug: User fetched: ${_user.value}")
                val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                val currentMealName = getCurrentMealName(currentHour)

                storageService.userProducts.collect { products ->
                    val currentCalorie = nutritionService.getTotalCaloriesForDate(products, today)
                    val todayProducts = products.find { it.date == today }
                    println("HomeVMDebug: Today's products: $todayProducts")
                    todayProducts?.let { userProducts ->
                        val mealNutrients =
                            nutritionService.getMealDataByType(userProducts, currentMealName)
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
    }


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