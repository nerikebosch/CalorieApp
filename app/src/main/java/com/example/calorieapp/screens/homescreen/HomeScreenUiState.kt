package com.example.calorieapp.screens.homescreen

data class HomeScreenUiState(
    val currentCalorie: Double = 0.0,
    val currentWeight: Double = 0.0,
    val currentWater: Double = 0.0,

    // Data for meal card
    val mealTitle: String = "",
    val mealCalories: Double = 0.0,
    val mealProteins: Double = 0.0,
    val mealCarbs: Double = 0.0,
    val mealFats: Double = 0.0,
    val mealRDC: Double = 0.0,
)