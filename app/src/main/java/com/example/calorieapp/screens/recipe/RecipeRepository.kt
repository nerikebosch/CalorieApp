package com.example.calorieapp.screens.recipe

import android.content.Context
import com.example.calorieapp.model.RecipeDetails
import com.example.calorieapp.model.getRecipesFromJson
import jakarta.inject.Inject

class RecipeRepository @Inject constructor(
    private val context: Context
) {

    // Change the return type to List<RecipeDetails>
    fun getRecipes(): List<RecipeDetails> {
        // Ensure that getRecipesFromJson is updated to return List<RecipeDetails>
        return getRecipesFromJson(context)
    }
}
