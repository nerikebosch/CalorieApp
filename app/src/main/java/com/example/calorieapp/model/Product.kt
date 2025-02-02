package com.example.calorieapp.model

import com.google.gson.annotations.SerializedName


/**
 * Data model representing a product retrieved from the OpenFoodFacts API.
 *
 * @property productName The name of the product (nullable).
 * @property nutrients Nutritional information associated with the product (nullable).
 */
data class Product(
    @SerializedName("product_name")
    val productName: String? = null,

    @SerializedName("nutriments")
    val nutrients: Nutrients? = null
)

/**
 * Data model for the OpenFoodFacts API response containing a list of products.
 *
 * @property products A list of [Product] objects returned by the API.
 */
data class OpenFoodFactsResponse(
    @SerializedName("products")
    val products: List<Product>
)

/**
 * Data model representing the nutrient information of a product.
 *
 * @property carbohydrates Amount of carbohydrates per 100g (nullable).
 * @property protein Amount of protein per 100g (nullable).
 * @property fat Amount of fat per 100g (nullable).
 * @property calories Energy content in kcal per 100g (nullable).
 */
data class Nutrients(
    @SerializedName("carbohydrates_100g")
    val carbohydrates: Double? = null,
    @SerializedName("proteins_100g")
    val protein: Double? = null,
    @SerializedName("fat_100g")
    val fat: Double? = null,
    @SerializedName("energy-kcal_100g")
    val calories: Double? = null
)

