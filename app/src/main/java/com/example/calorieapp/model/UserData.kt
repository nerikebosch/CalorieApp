package com.example.calorieapp.model

import com.google.firebase.firestore.DocumentId

/**
 * Represents a user's daily data, including water intake and calorie consumption.
 *
 * @property id Unique identifier for the user data record, assigned by Firebase.
 * @property date The date for which the data is recorded, in YYYY-MM-DD format.
 * @property water The amount of water consumed by the user on the specified date, in liters.
 * @property calories The number of calories consumed by the user on the specified date.
 */
data class UserData(
    @DocumentId val id: String = "",
    val date: String = "",
    val water: Double = 0.0,
    val calories: Double = 0.0,
)
