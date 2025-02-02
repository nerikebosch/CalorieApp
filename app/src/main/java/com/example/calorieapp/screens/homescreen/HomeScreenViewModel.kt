package com.example.calorieapp.screens.homescreen

import com.example.calorieapp.SETTINGS_SCREEN
import com.example.calorieapp.model.MealName
import com.example.calorieapp.model.User
import com.example.calorieapp.model.UserData
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


/**
 * ViewModel for the Home Screen, responsible for managing user-related data,
 * calorie tracking, water intake, and meal information.
 *
 * @property logService The logging service used for error tracking.
 * @property accountService The service that handles user authentication and data retrieval.
 * @property storageService The service for accessing stored user data.
 * @property nutritionService The service that calculates nutritional information.
 */
@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    logService: LogService,
    private val accountService: AccountService,
    private val storageService: StorageService,
    private val nutritionService: NutritionService
) : CalorieAppViewModel(logService) {

    /** Holds the current user and userdata. */
    private val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()

    private val _userData = MutableStateFlow(UserData())
    val userData = _userData.asStateFlow()

    /** Holds the UI state data for the home screen. */
    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    /**
     * Loads the current user's data from the [AccountService].
     * Updates the [_user] & [_userData] state flow when data is retrieved.
     */
    fun onUserLoad() {
        launchCatching {
            accountService.currentUser.collect { fetchedUser ->
                _user.value = fetchedUser
            }
        }
    }
    /*
     * Loads today's user data from the [StorageService].
     */
    private suspend fun loadTodayUserData() {
        val todayData = storageService.getUserDataByDate(today)

        if (todayData != null) {
            _userData.value = todayData
            _uiState.value = _uiState.value.copy(currentWater = todayData.water)
            println("Debug: Loaded existing userData: $todayData")
        } else {
            // Create new UserData for today if it doesn't exist
            val newUserData = UserData(date = today)
            val newId = storageService.saveUserData(newUserData)
            _userData.value = newUserData.copy(id = newId)
            println("Debug: Created new userData with id: $newId")
        }
    }

    /**
     * Updates the user's water intake.
     *
     * @param addedAmount The amount of water to add to the current intake.
     */
    fun updateWaterIntake(addedAmount: Double) {
        val newWaterAmount = _uiState.value.currentWater + addedAmount
        _uiState.value = _uiState.value.copy(currentWater = newWaterAmount)

        launchCatching {
            println("Debug: Updating water intake to $newWaterAmount")
            println("Debug: Current userData id: ${_userData.value.id}")
            println("HomeVMDebug: Updating water intake to ${_uiState.value.currentWater}")

            val updatedUserData = _userData.value.copy(water = newWaterAmount)
            _userData.value = updatedUserData

            storageService.updateUserData(updatedUserData)
        }
    }

    /**
     * Updates the user's weight and stores the updated value in the [AccountService].
     *
     * @param newWeight The new weight value to update.
     */
    fun updateUserWeight(newWeight: Double) {
        _user.value = _user.value.copy(weight = newWeight)
        launchCatching {
            accountService.linkAccount(_user.value)
        }
    }

    /**
     * Initializes the ViewModel by loading user data, retrieving stored meals,
     * and updating UI state with calorie and meal information.
     */
    init {
        launchCatching {
            loadTodayUserData()

            accountService.currentUser.collect { fetchedUser ->
                _user.value = fetchedUser

                println("HomeVMDebug: User fetched: ${_user.value}")

                val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                val currentMealName = getCurrentMealName(currentHour)

                storageService.userProducts.collect { products ->

                    val currentCalorie = nutritionService.getTotalCaloriesForDate(products, today)
                    val todayProducts = products.find { it.date == today }
                    println("HomeVMDebug: Today's products: $todayProducts")

                    todayProducts?.let { userProducts ->
                        val mealNutrients = nutritionService.getMealDataByType(userProducts, currentMealName)

                        _uiState.value = _uiState.value.copy(
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


    /**
     * Handles the navigation event when the settings button is clicked.
     *
     * @param openScreen A lambda function to navigate to the settings screen.
     */
    fun onSettingsClick(openScreen: (String) -> Unit) = openScreen(SETTINGS_SCREEN)

    /**
     * Determines the meal type based on the current hour.
     *
     * @param hour The current hour in a 24-hour format.
     * @return A [MealName] representing the meal type.
     */
    private fun getCurrentMealName(hour: Int): MealName {
        return when (hour) {
            in 5..10 -> MealName.Breakfast
            in 11..14 -> MealName.Lunch
            in 15..20 -> MealName.Dinner
            else -> MealName.Snack
        }
    }
}