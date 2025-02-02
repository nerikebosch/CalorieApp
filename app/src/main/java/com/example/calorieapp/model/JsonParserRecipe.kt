package com.example.calorieapp.model

import android.content.Context
import android.util.Log
import com.google.gson.Gson


/**
 * Reads and parses a JSON file containing recipe details from the app's assets folder.
 *
 * @param context The application context used to access assets.
 * @return A list of [RecipeDetails] objects parsed from the JSON file, or an empty list if an error occurs.
 */
fun getRecipesFromJson(context: Context): List<RecipeDetails> {
    return try {
        val inputStream = context.assets.open("Recipes.json")
        val json = inputStream.bufferedReader().use { it.readText() }

        Log.d("JsonParser", "JSON Content: $json") // Debugging line

        val gson = Gson()
        val recipeResponse: RecipeResponse = gson.fromJson(json, RecipeResponse::class.java) // Correct type

        Log.d("JsonParser", "Parsed Recipes: ${recipeResponse.recipes}") // Check if parsing worked

        recipeResponse.recipes
    } catch (e: Exception) {
        Log.e("JsonParser", "Error parsing JSON", e)
        emptyList()
    }
}
