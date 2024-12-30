package com.example.calorieapp.model

import com.google.gson.annotations.SerializedName


data class Product(
    @SerializedName("product_name")
    val productName: String?,

    @SerializedName("nutriments")
    val nutrients: Nutrients?
)

data class OpenFoodFactsResponse(
    @SerializedName("products")
    val products: List<Product>
)

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