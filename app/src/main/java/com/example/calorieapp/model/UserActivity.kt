package com.example.calorieapp.model

import com.example.calorieapp.model.service.impl.LocationServiceImpl
import com.example.calorieapp.screens.adddata.formatDateToString
import com.google.firebase.firestore.DocumentId
import java.time.LocalDate

data class UserActivity(
    @DocumentId val id: String = "",
    val date: String = LocalDate.now().format(LocationServiceImpl.FIREBASE_DATE_FORMATTER), // Store as YYYY-MM-DD in Firebase
    val steps: Int = 0,
    val distanceInMeters: Double = 0.0,
    val caloriesBurned: Double = 0.0,
)
