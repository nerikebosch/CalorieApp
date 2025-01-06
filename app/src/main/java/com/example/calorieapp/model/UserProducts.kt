package com.example.calorieapp.model

import com.google.firebase.firestore.DocumentId


enum class MealName {
    Breakfast,
    Lunch,
    Dinner,
    Snack;

    companion object {
        fun getByName(name: String): MealName {
            return entries.find { it.name == name } ?: Breakfast
        }

        fun getMealData(mealName: MealName, products: List<Product>): MealData {
            return MealData(mealName = mealName, products = products)
        }
    }
}

data class MealData(
    val mealName: MealName = MealName.Breakfast,
    val products: List<Product> = emptyList()
)

data class UserProducts(
    @DocumentId val id: String = "",
    val date: String = "",
    val breakfast: MealData = MealData(mealName = MealName.Breakfast),
    val lunch: MealData = MealData(mealName = MealName.Lunch),
    val dinner: MealData = MealData(mealName = MealName.Dinner),
    val snacks: MealData = MealData(mealName = MealName.Snack)
)