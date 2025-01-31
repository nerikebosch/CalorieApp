package com.example.calorieapp.screens.recipe

import android.content.Context
import com.example.calorieapp.model.RecipeDetails
import com.example.calorieapp.model.getRecipesFromJson
import jakarta.inject.Inject


/**
 * Repository class responsible for fetching recipe data from a local JSON source.
 * This class provides methods to retrieve all recipes or a specific recipe by its name.
 *
 * @property context The application context used to access resources.
 */
class RecipeRepository @Inject constructor(
    private val context: Context
) {

    /**
     * Retrieves a list of all available recipes from the JSON data source.
     *
     * @return A list of [RecipeDetails] containing all the recipes.
     */
    // Change the return type to List<RecipeDetails>
    fun getRecipes(): List<RecipeDetails> {
        // Ensure that getRecipesFromJson is updated to return List<RecipeDetails>
        return getRecipesFromJson(context)
    }

    /**
     * Retrieves a specific recipe by its name.
     *
     * @param recipeName The name of the recipe to search for.
     * @return The [RecipeDetails] object if found, otherwise null.
     */
    fun getRecipeByName(recipeName: String): RecipeDetails? {
        return getRecipesFromJson(context).find { it.name == recipeName }
    }
}
