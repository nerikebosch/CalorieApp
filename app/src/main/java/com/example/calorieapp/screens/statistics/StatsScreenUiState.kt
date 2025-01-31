package com.example.calorieapp.screens.statistics


/**
 * Represents the UI state for the statistics screen.
 *
 * @property goalCalorie The user's goal for daily calorie intake.
 * @property listCalories A list containing the calorie intake for each day of the week.
 */
data class StatsScreenUiState(
    val goalCalorie: Double = 0.0,
    val listCalories: List<Double> = emptyList(),
)