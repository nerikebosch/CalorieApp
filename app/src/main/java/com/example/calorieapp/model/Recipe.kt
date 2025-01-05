package com.example.calorieapp.model

import com.google.gson.annotations.SerializedName


data class RecipeResponse(
    val recipes: List<RecipeDetails>
)

data class RecipeDetails(
    val category: String,
    val name: String,

    @SerializedName("image_url")
    val imageUrl: String,

    @SerializedName("nutritional_values")
    val nutritionalValues: NutritionalValues,

    val ingredients: List<String>,
    val instructions: List<String>
)

data class NutritionalValues(
    val calories: Int,
    val fat: Int,
    val protein: Int,
    val carbohydrates: Int
)



