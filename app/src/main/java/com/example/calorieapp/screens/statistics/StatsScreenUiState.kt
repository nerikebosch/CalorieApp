package com.example.calorieapp.screens.statistics

data class StatsScreenUiState(
    val goalCalorie: Double = 0.0,
    val listCalories: List<Double> = emptyList(),
)