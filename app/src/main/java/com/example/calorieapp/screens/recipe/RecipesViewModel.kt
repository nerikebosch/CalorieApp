package com.example.calorieapp.screens.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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


/**
 * ViewModel for managing and providing recipe data to the UI.
 *
 * @property context The application context used for accessing resources.
 * @property recipeRepository The repository responsible for fetching recipe data.
 * @property logService Service for handling logging operations.
 * @property accountService Service for managing user account data.
 * @property storageService Service for handling storage-related operations.
 */
@HiltViewModel
class RecipesViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val recipeRepository: RecipeRepository,
    logService: LogService,
    private val accountService: AccountService,
    storageService: StorageService,
) : CalorieAppViewModel(logService) {

    /**
     * LiveData to store a list of recipe details.
     */
    private val _recipes = MutableLiveData<List<RecipeDetails>>()
    val recipes: LiveData<List<RecipeDetails>> get() = _recipes

    /**
     * Initializes the ViewModel by loading recipes.
     */
    init {
        loadRecipes()
    }

    /**
     * Loads recipes from the repository and updates LiveData.
     */
    private fun loadRecipes() {
        val recipesList = recipeRepository.getRecipes() // Ensure this returns a List<RecipeDetails>
        Log.d("RecipesViewModel", "Loaded recipes: $recipesList")
        _recipes.value = recipesList
    }

    /**
     * Handles the event when a recipe is clicked.
     *
     * @param openAndPopUp Function to navigate to the recipe details screen.
     * @param recipeName Name of the selected recipe.
     */
    fun onRecipeClick(openAndPopUp: (String, String) -> Unit, recipeName: String) {
        openAndPopUp("$RECIPE_DETAILS_SCREEN/$recipeName", RECIPES_SCREEN)
    }
}
