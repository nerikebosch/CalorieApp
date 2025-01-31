package com.example.calorieapp.model

data class User(
    val id: String="",
    val name: String="",
    val surname: String="",
    val registeredUser: Boolean = false,
    val email: String="",
    val weight: Double = 0.0,
    val height: Double = 0.0,
    // Goal information
    val goalWater: Double = 0.0,
    val goalCalorie: Double = 0.0,
    val goalWeight: Double = 0.0,
)