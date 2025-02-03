package com.example.calorieapp.model

import com.example.calorieapp.model.service.impl.LocationServiceImpl
import com.example.calorieapp.screens.adddata.formatDateToString
import com.google.firebase.firestore.DocumentId
import java.time.LocalDate

/**
 * Represents a user's physical activity for a specific day.
 *
 * @property id Unique identifier for the activity record, assigned by Firebase.
 * @property date The date of the activity in YYYY-MM-DD format.
 * @property steps The number of steps taken by the user on the specified date.
 * @property distanceInMeters The distance covered by the user in meters.
 * @property caloriesBurned The total calories burned by the user on the specified date.
 */
data class UserActivity(
    @DocumentId val id: String = "",
    val date: String = "", // Store as YYYY-MM-DD in Firebase
    val steps: Int = 0,
    val distanceInMeters: Double = 0.0,
    val caloriesBurned: Double = 0.0,
)
