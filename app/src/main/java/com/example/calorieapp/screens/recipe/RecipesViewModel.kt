package com.example.calorieapp.screens.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calorieapp.model.RecipeDetails
import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.LogService
import com.example.calorieapp.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import android.content.Context
import android.util.Log
import com.example.calorieapp.RECIPES_SCREEN
import com.example.calorieapp.RECIPE_DETAILS_SCREEN
import com.example.calorieapp.screens.CalorieAppViewModel
import dagger.hilt.android.qualifiers.ApplicationContext

@HiltViewModel
class RecipesViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val recipeRepository: RecipeRepository,
    logService: LogService,
    private val accountService: AccountService,
    storageService: StorageService,
) : CalorieAppViewModel(logService) {

    // Use LiveData to hold a list of RecipeDetails
    private val _recipes = MutableLiveData<List<RecipeDetails>>()
    val recipes: LiveData<List<RecipeDetails>> get() = _recipes

    init {
        loadRecipes()
    }

    // Update loadRecipes to retrieve RecipeDetails
    private fun loadRecipes() {
        val recipesList = recipeRepository.getRecipes() // Ensure this returns a List<RecipeDetails>
        Log.d("RecipesViewModel", "Loaded recipes: $recipesList")
        _recipes.value = recipesList
    }

    // Handle recipe click event
    fun onRecipeClick(openAndPopUp: (String, String) -> Unit, recipeName: String) {
        openAndPopUp("$RECIPE_DETAILS_SCREEN/$recipeName", RECIPES_SCREEN)
    }
}
