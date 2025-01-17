package com.example.calorieapp.model

data class User(
    val id: String="",
    val name: String="",
    val surname: String="",
    val registeredUser: Boolean = false,
    val email: String="",
)