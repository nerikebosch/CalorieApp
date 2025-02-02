package com.example.calorieapp.model

import com.google.firebase.firestore.DocumentId

/**
 * Enum representing different meal types.
 *
 * Provides a utility function [getMealData] to create a [MealData] instance for a given meal type.
 */
enum class MealName {
    Breakfast,
    Lunch,
    Dinner,
    Snack;

    /**
     * Creates a [MealData] object for the given meal type and list of products.
     *
     * @param mealName The type of meal.
     * @param products The list of products consumed in the meal.
     * @return A [MealData] object containing the meal name and associated products.
     */
    fun getMealData(mealName: MealName, products: List<Product>): MealData {
        return MealData(mealName = mealName, products = products)
    }

}


/**
 * Data model representing a meal and its associated products.
 *
 * @property mealName The type of meal (default is [MealName.Breakfast]).
 * @property products The list of products consumed in the meal (default is an empty list).
 */
data class MealData(
    val mealName: MealName = MealName.Breakfast,
    val products: List<Product> = emptyList()
)


/**
 * Data model representing a user's logged meals and products for a specific date.
 *
 * @property id The unique document ID in Firestore.
 * @property date The date for which the meal data is recorded.
 * @property breakfast The meal data for breakfast.
 * @property lunch The meal data for lunch.
 * @property dinner The meal data for dinner.
 * @property snacks The meal data for snacks.
 */
data class UserProducts(
    @DocumentId val id: String = "",
    val date: String = "",
    val breakfast: MealData = MealData(mealName = MealName.Breakfast),
    val lunch: MealData = MealData(mealName = MealName.Lunch),
    val dinner: MealData = MealData(mealName = MealName.Dinner),
    val snacks: MealData = MealData(mealName = MealName.Snack)
)