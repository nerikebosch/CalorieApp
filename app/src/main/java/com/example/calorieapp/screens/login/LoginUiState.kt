package com.example.calorieapp.screens.login

/**
 * Data class representing the UI state for the login screen.
 *
 * @param email User's email input.
 * @param password User's password input.
 */
data class LoginUiState(
    val email: String = "",
    val password: String = ""
)