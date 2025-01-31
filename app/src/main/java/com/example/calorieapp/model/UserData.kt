package com.example.calorieapp.model

import com.google.firebase.firestore.DocumentId

data class UserData(
    @DocumentId val id: String = "",
    val date: String = "",
    val weight: Double = 0.0,
    val water: Double = 0.0,
    val calories: Double = 0.0,
)
