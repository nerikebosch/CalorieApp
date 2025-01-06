package com.example.calorieapp.screens.recipe

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.calorieapp.model.RecipeDetails
import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.LogService
import com.example.calorieapp.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val recipeRepository: RecipeRepository, // Fetches recipes
    logService: LogService,
    private val accountService: AccountService,
    storageService: StorageService,
) : ViewModel() {

    private val _selectedRecipe = MutableStateFlow<RecipeDetails?>(null)
    val selectedRecipe: StateFlow<RecipeDetails?> get() = _selectedRecipe

    fun loadRecipe(recipeName: String) {
        viewModelScope.launch {
            val recipe = recipeRepository.getRecipeByName(recipeName)
            _selectedRecipe.value = recipe
        }
    }
}
