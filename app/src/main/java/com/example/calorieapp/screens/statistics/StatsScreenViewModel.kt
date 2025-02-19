package com.example.calorieapp.screens.statistics

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
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * ViewModel for the statistics screen, responsible for managing user data
 * and fetching weekly calorie statistics.
 *
 * @param logService Service for logging application events and errors.
 * @param accountService Service for managing user accounts.
 * @param storageService Service for accessing stored user data.
 * @param nutritionService Service for retrieving nutritional information.
 */
@HiltViewModel
class StatsScreenViewModel @Inject constructor(
    logService: LogService,
    private val accountService: AccountService,
    private val storageService: StorageService,
    private val nutritionService: NutritionService
) : CalorieAppViewModel(logService) {

    /** The currently authenticated user. */
    private val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()

    /** UI state containing weekly calorie statistics. */
    private val _uiState = MutableStateFlow(StatsScreenUiState())
    val uiState: StateFlow<StatsScreenUiState> = _uiState.asStateFlow()

    init {
        // Initial fetch for the current week
        val today = Calendar.getInstance().timeInMillis
        fetchWeeklyCaloriesForWeek(today)
    }

    /**
     * Fetches the calorie intake data for a specific week (Monday to Sunday)
     * based on the provided timestamp.
     *
     * @param selectedDateMillis The timestamp representing a date within the week to fetch data for.
     */
    fun fetchWeeklyCaloriesForWeek(selectedDateMillis: Long) {
        launchCatching {
            accountService.currentUser.collect { fetchedUser ->
                _user.value = fetchedUser

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val calendar = Calendar.getInstance().apply { timeInMillis = selectedDateMillis }

                // Set the calendar to Monday of the selected week
                calendar.firstDayOfWeek = Calendar.MONDAY
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

                // Generate the dates for the full week (Monday to Sunday)
                val weekDates = (0..6).map { dayOffset ->
                    (calendar.clone() as Calendar).apply {
                        add(Calendar.DAY_OF_YEAR, dayOffset)
                    }.time
                }

                // Initialize the weekly calories list
                val weeklyCalories = MutableList(7) { 0.0 }

                val products = storageService.userProducts.first() // Fetch user's product data

                // Fetch calories for each day in the week
                weekDates.forEachIndexed { index, date ->
                    val formattedDate = dateFormat.format(date)
                    val dailyCalories = nutritionService.getTotalCaloriesForDate(products, formattedDate)
                    weeklyCalories[index] = dailyCalories
                }

                // Update the UI state with the calculated data
                _uiState.value = _uiState.value.copy(
                    listCalories = weeklyCalories,
                    goalCalorie = _user.value.goalCalorie
                )
            }
        }
    }
}