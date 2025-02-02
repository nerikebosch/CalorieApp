package com.example.calorieapp.screens.homescreen


/**
 * Represents the UI state for the Home Screen in the Calorie App.
 *
 * This data class holds the current calorie intake, weight, and water intake,
 * along with nutritional details for a meal.
 *
 * @property currentCalorie The total number of calories consumed so far.
 * @property currentWeight The user's current weight.
 * @property currentWater The amount of water consumed.
 * @property mealTitle The name/title of the meal.
 * @property mealCalories The number of calories in the meal.
 * @property mealProteins The protein content of the meal (in grams).
 * @property mealCarbs The carbohydrate content of the meal (in grams).
 * @property mealFats The fat content of the meal (in grams).
 * @property mealRDC The recommended daily calorie intake percentage covered by this meal.
 */
data class HomeScreenUiState(
    val currentCalorie: Double = 0.0,
    val currentWeight: Double = 0.0,
    var currentWater: Double = 0.0,

    // Data for meal card
    val mealTitle: String = "",
    val mealCalories: Double = 0.0,
    val mealProteins: Double = 0.0,
    val mealCarbs: Double = 0.0,
    val mealFats: Double = 0.0,
    val mealRDC: Double = 0.0,
)