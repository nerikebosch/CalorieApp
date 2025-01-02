package com.example.calorieapp.screens.adddata

import androidx.lifecycle.ViewModel
import com.example.calorieapp.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class SharedViewModel: ViewModel() {
    private val _userProducts = MutableStateFlow<Map<String, List<Product>>>(emptyMap())
    val userProducts: StateFlow<Map<String, List<Product>>> = _userProducts

    fun setUserProducts(mealName: String, products: List<Product>) {
        _userProducts.update { currentMap ->
            currentMap.toMutableMap().apply {
                put(mealName, products)
            }
        }
    }
}