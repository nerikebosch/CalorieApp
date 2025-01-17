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
    val calories: Double? = null,
    val fat: Double? = null,
    val protein: Double? = null,
    val carbohydrates: Double? = null
)

fun RecipeDetails.toProduct(): Product {
    return Product(
        productName = this.name,
        nutrients = this.nutritionalValues.toNutrients()
    )
}

fun NutritionalValues.toNutrients(): Nutrients {
    return Nutrients(
        carbohydrates = this.carbohydrates,
        protein = this.protein,
        fat = this.fat,
        calories = this.calories
    )
}

