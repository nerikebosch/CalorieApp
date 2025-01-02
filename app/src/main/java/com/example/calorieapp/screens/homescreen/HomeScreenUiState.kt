package com.example.calorieapp.screens.homescreen

data class HomeScreenUiState(
    val currentCalorie: Float = 0.0f,
    val goalCalorie: Float = 0.0f,
    val currentWeight: Float = 0.0f,
    val goalWeight : Float = 0.0f,
    val currentWater: Float = 0.0f,
    val goalWater : Float = 0.0f,

    // Data for meal card
    val mealTitle: String = "",
    val mealCalories: Int = 0,
    val mealProteins: Int = 0,
    val mealCarbs: Int = 0,
    val mealFats: Int = 0,
    val mealRDC: Int = 0,
)