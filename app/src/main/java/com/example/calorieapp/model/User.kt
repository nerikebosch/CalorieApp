package com.example.calorieapp.model

/**
 * Represents a user in the application, including personal details, fitness goals, and account status.
 *
 * @property id Unique identifier for the user.
 * @property name First name of the user.
 * @property surname Last name of the user.
 * @property registeredUser Indicates if the user is registered in the system.
 * @property email Email address of the user.
 * @property weight Current weight of the user in kilograms.
 * @property height Current height of the user in centimeters.
 * @property gender Gender of the user.
 * @property dob Date of birth of the user in a string format.
 * @property age Age of the user in years.
 * @property goalWater Daily water intake goal for the user in liters.
 * @property goalCalorie Daily calorie intake goal for the user in kilocalories.
 * @property goalWeight Weight goal for the user in kilograms.
 */
data class User(
    val id: String="",
    val name: String="",
    val surname: String="",
    val registeredUser: Boolean = false,
    val email: String="",
    val weight: Double = 0.0,
    val height: Double = 0.0,
    val gender: String="",
    val dob: String="",
    val age: Int=0,

    // Goal information
    val goalWater: Double = 0.0,
    val goalCalorie: Double = 0.0,
    val goalWeight: Double = 0.0,
)