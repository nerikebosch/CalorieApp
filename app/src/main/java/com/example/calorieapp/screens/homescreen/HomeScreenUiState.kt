package com.example.calorieapp.screens.homescreen

data class HomeScreenUiState(
    val goalCalorie: Int = 0,
    val currentCalorie: Int = 0,
    val currentWeight: Double = 0.0,
    val goalWeight : Double = 0.0,
    val currentWater: Int = 0,
    val goalWater : Int = 0,

    // Data for meal card
    val mealTitle: String = "",
    val mealCalories: Int = 0,
    val mealProteins: Int = 0,
    val mealCarbs: Int = 0,
    val mealFats: Int = 0,
    val mealRDC: Int = 0,
)