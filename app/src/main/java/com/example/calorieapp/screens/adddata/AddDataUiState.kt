package com.example.calorieapp.screens.adddata

import com.example.calorieapp.model.Product


/**
 * Represents the UI state for the Add Data screen.
 *
 * This state contains the list of selected products, search suggestions,
 * loading status, error messages, and the current search query.
 *
 * @property selectedProducts List of products selected by the user.
 * @property suggestions List of suggested products based on the search query.
 * @property isLoading Boolean flag indicating whether data is currently being loaded.
 * @property errorMessage Error message string, if an error occurs.
 * @property searchQuery The current search query entered by the user.
 */
data class AddDataUiState(
    val selectedProducts: List<Product> = emptyList(),
    val suggestions: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val searchQuery: String = ""
)