package com.example.calorieapp.model

import com.google.gson.annotations.SerializedName

/**
 * Data model representing a response containing a list of recipe details.
 *
 * @property recipes A list of [RecipeDetails] objects.
 */
data class RecipeResponse(
    val recipes: List<RecipeDetails>
)

/**
 * Data model representing the details of a recipe.
 *
 * @property category The category of the recipe.
 * @property name The name of the recipe.
 * @property imageUrl The URL of the recipe image.
 * @property nutritionalValues The nutritional information of the recipe.
 * @property ingredients A list of ingredients used in the recipe.
 * @property instructions A list of instructions for preparing the recipe.
 */
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

/**
 * Data model representing the nutritional values of a recipe.
 *
 * @property calories The number of calories in the recipe (nullable).
 * @property fat The amount of fat in the recipe (nullable).
 * @property protein The amount of protein in the recipe (nullable).
 * @property carbohydrates The amount of carbohydrates in the recipe (nullable).
 */
data class NutritionalValues(
    val calories: Double? = null,
    val fat: Double? = null,
    val protein: Double? = null,
    val carbohydrates: Double? = null
)

/**
 * Extension function to convert a [RecipeDetails] object into a [Product] object.
 *
 * @return A [Product] object with mapped nutritional information.
 */
fun RecipeDetails.toProduct(): Product {
    return Product(
        productName = this.name,
        nutrients = this.nutritionalValues.toNutrients()
    )
}

/**
 * Extension function to convert [NutritionalValues] into a [Nutrients] object.
 *
 * @return A [Nutrients] object with mapped nutritional values.
 */
fun NutritionalValues.toNutrients(): Nutrients {
    return Nutrients(
        carbohydrates = this.carbohydrates,
        protein = this.protein,
        fat = this.fat,
        calories = this.calories
    )
}

