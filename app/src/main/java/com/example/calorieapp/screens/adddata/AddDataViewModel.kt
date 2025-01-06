package com.example.calorieapp.screens.adddata

import android.util.Log.e
import com.example.calorieapp.model.MealData
import com.example.calorieapp.model.MealName
import com.example.calorieapp.model.Product
import com.example.calorieapp.model.UserProducts
import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.LogService
import com.example.calorieapp.model.service.StorageService
import com.example.calorieapp.screens.CalorieAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AddDataViewModel @Inject constructor(
    logService: LogService,
    private val accountService: AccountService,
    private val storageService: StorageService,
) : CalorieAppViewModel(logService) {

    private val _selectedProducts = MutableStateFlow<List<Product>>(emptyList())
    val selectedProducts: StateFlow<List<Product>> = _selectedProducts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _suggestions = MutableStateFlow<List<Product>>(emptyList())
    val suggestions: StateFlow<List<Product>> = _suggestions.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun updateSelectedProducts(products: List<Product>) {
        _selectedProducts.value = products
    }

    fun loadSelectedProducts(mealName: String, date: String) {
        launchCatching {
            _isLoading.value = true
            try {
                val userProducts = storageService.getUserProductByDate(date)
                val products = when (mealName.lowercase()) {
                    "breakfast" -> userProducts?.breakfast?.products
                    "lunch" -> userProducts?.lunch?.products
                    "dinner" -> userProducts?.dinner?.products
                    "snack" -> userProducts?.snacks?.products
                    else -> null
                } ?: emptyList()

                _selectedProducts.value = products
            } catch (e: Exception) {
                println("Debug: Error loading selected products $e")
                _errorMessage.value = "Failed to load products: ${e.message}"
                _selectedProducts.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addProduct(product: Product) {
        println("AddDataVMDebug: addProduct called with product: $product")
        _selectedProducts.value = _selectedProducts.value + product
    }

    fun removeProduct(product: Product) {
        _selectedProducts.value = _selectedProducts.value - product
    }

    fun updateSuggestions(newSuggestions: List<Product>) {
        _suggestions.value = newSuggestions
    }

    fun setError(message: String?) {
        _errorMessage.value = message
    }

    fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }

    suspend fun saveProductsToMeal(mealName: String, date: String) {
        try {
            println("AddDataVMDebug: mealType: $mealName")
            println("AddDataVMDebug: date: $date")
            _isLoading.value = true

            // Get existing UserProducts for the date or create new one
            var userProducts = storageService.getUserProductByDate(date) ?: UserProducts(date = date)
            println("AddDataVMDebug: saveProductsToMeal- userProducts: $userProducts")
            // Update the appropriate meal based on mealType
            userProducts = when (mealName.lowercase()) {
                "breakfast" -> userProducts.copy(
                    breakfast = MealData(MealName.Breakfast, _selectedProducts.value)
                )
                "lunch" -> userProducts.copy(
                    lunch = MealData(MealName.Lunch, _selectedProducts.value)
                )
                "dinner" -> userProducts.copy(
                    dinner = MealData(MealName.Dinner, _selectedProducts.value)
                )
                "snack" -> userProducts.copy(
                    snacks = MealData(MealName.Snack, _selectedProducts.value)
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
            _errorMessage.value = "Failed to save products: ${e.message}"
            throw e
        } finally {
            _isLoading.value = false
        }
    }

    fun onSaveClick(mealName: String, date: String, openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            saveProductsToMeal(mealName, date)
            println("AddDataVMDebug: Save products to $mealName for $date")
            openAndPopUp("MealTimeScreen", "AddDataScreen")
        }
    }
}