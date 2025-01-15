package com.example.calorieapp.screens.adddata

import com.example.calorieapp.model.Product

// UI State data class to hold all screen state
data class AddDataUiState(
    val selectedProducts: List<Product> = emptyList(),
    val suggestions: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val searchQuery: String = ""
)