package com.example.calorieapp.screens.adddata

import androidx.lifecycle.viewModelScope
import com.example.calorieapp.api.RetrofitClient
import com.example.calorieapp.common.snackbar.SnackbarManager
import com.example.calorieapp.model.MealData
import com.example.calorieapp.model.MealName
import com.example.calorieapp.model.Product
import com.example.calorieapp.model.UserProducts
import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.LogService
import com.example.calorieapp.model.service.StorageService
import com.example.calorieapp.screens.CalorieAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import com.example.calorieapp.R.string as AppText


/**
 * ViewModel for managing the UI state and business logic of the Add Data screen.
 * Handles product searches, user-selected product management, and saving meal data.
 *
 * @param logService The service for logging application events.
 * @param accountService The service for handling user account data.
 * @param storageService The service for managing stored user data.
 */
@HiltViewModel
class AddDataViewModel @Inject constructor(
    logService: LogService,
    private val accountService: AccountService,
    private val storageService: StorageService,
) : CalorieAppViewModel(logService) {

    private val _uiState = MutableStateFlow(AddDataUiState())
    val uiState: StateFlow<AddDataUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null


    /**
     * Updates the search query state and triggers a product search after a debounce period.
     *
     * @param query The search query entered by the user.
     */
    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        searchJob?.cancel()

        if (query.length >= 2) {
            searchJob = viewModelScope.launch {
                delay(300) // Debounce
                searchProducts(query)
            }
        } else if (query.isEmpty()) {
            clearSearchResults()
        }
    }

    /**
     * Clears the search results from the UI state.
     */
    private fun clearSearchResults() {
        _uiState.value = _uiState.value.copy(
            suggestions = emptyList(),
            isLoading = false,
            errorMessage = null
        )
    }

    /**
     * Searches for products based on the provided query using the API.
     *
     * @param query The search term entered by the user.
     */
    private suspend fun searchProducts(query: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        try {
            val response = RetrofitClient.api.searchProducts(searchTerms = query)
            val filteredProducts = response.products.filter {
                !it.productName.isNullOrBlank() && it.nutrients != null
            }

            _uiState.value = _uiState.value.copy(
                suggestions = filteredProducts,
                isLoading = false,
                errorMessage = if (filteredProducts.isEmpty()) "No products found for '$query'" else null
            )
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is IOException -> "Network error. Please check your connection."
                is HttpException -> "Server error. Please try again later."
                else -> "An unexpected error occurred: ${e.message}"
            }
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                errorMessage = errorMessage
            )
        }
    }

    /**
     * Loads the selected products for a given meal and date from storage.
     *
     * @param mealName The name of the meal (e.g., breakfast, lunch).
     * @param date The date for which the meal data is being retrieved.
     */
    fun loadSelectedProducts(mealName: String, date: String) {
        launchCatching {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val userProducts = storageService.getUserProductByDate(date)
                val products = when (mealName.lowercase()) {
                    "breakfast" -> userProducts?.breakfast?.products
                    "lunch" -> userProducts?.lunch?.products
                    "dinner" -> userProducts?.dinner?.products
                    "snack" -> userProducts?.snacks?.products
                    else -> null
                } ?: emptyList()

                _uiState.value = _uiState.value.copy(
                    selectedProducts = products,
                    isLoading = false
                )
            } catch (e: Exception) {
                println("Debug: Error loading selected products $e")
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to load products: ${e.message}",
                    selectedProducts = emptyList(),
                    isLoading = false
                )
            }
        }
    }

    /**
     * Adds a product to the list of selected products.
     *
     * @param product The product to be added.
     */
    fun addProduct(product: Product) {
        val currentProducts = _uiState.value.selectedProducts
        _uiState.value = _uiState.value.copy(
            selectedProducts = currentProducts + product
        )
    }

    /**
     * Removes a product from the list of selected products.
     *
     * @param product The product to be removed.
     */
    fun removeProduct(product: Product) {
        val currentProducts = _uiState.value.selectedProducts
        _uiState.value = _uiState.value.copy(
            selectedProducts = currentProducts - product
        )
    }

    /**
     * Saves the selected products to the specified meal and date.
     *
     * @param mealName The name of the meal.
     * @param date The date for which the meal data is being saved.
     */
    private suspend fun saveProductsToMeal(mealName: String, date: String) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        try {
            println("AddDataVMDebug: mealType: $mealName")
            println("AddDataVMDebug: date: $date")

            // Get existing UserProducts for the date or create new one
            var userProducts = storageService.getUserProductByDate(date) ?: UserProducts(date = date)
            println("AddDataVMDebug: saveProductsToMeal- userProducts: $userProducts")
            // Update the appropriate meal based on mealType
            userProducts = when (mealName.lowercase()) {
                "breakfast" -> userProducts.copy(
                    breakfast = MealData(MealName.Breakfast, _uiState.value.selectedProducts)
                )
                "lunch" -> userProducts.copy(
                    lunch = MealData(MealName.Lunch, _uiState.value.selectedProducts)
                )
                "dinner" -> userProducts.copy(
                    dinner = MealData(MealName.Dinner, _uiState.value.selectedProducts)
                )
                "snack" -> userProducts.copy(
                    snacks = MealData(MealName.Snack, _uiState.value.selectedProducts)
                )
                else -> userProducts
            }
            println("AddDataVMDebug: userProducts: $userProducts")
            if (userProducts.id.isEmpty()) {
                storageService.saveUserProduct(userProducts)
            } else {
                storageService.updateUserProduct(userProducts)
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Failed to save products: ${e.message}",
                isLoading = false
            )
            throw e
        }
    }

    /**
     * Handles the save button click event by saving the selected products
     * and navigating back to the meal screen.
     *
     * @param mealName The name of the meal.
     * @param date The date of the meal.
     * @param openAndPopUp A lambda function to navigate and pop up a screen.
     */
    fun onSaveClick(mealName: String, date: String, openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            saveProductsToMeal(mealName, date)
            println("AddDataVMDebug: Save products to $mealName for $date")
            openAndPopUp("MealTimeScreen", "AddDataScreen")
            SnackbarManager.showMessage(AppText.added_meal)
        }
    }
}