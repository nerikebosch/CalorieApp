package com.example.calorieapp.model

import com.google.firebase.firestore.DocumentId

data class UserData(
    @DocumentId val id: String = "",
    val weight: Double = 0.0,
    val height: Double = 0.0,
    val calories: Double = 0.0,
    val age: Int = 0
)
