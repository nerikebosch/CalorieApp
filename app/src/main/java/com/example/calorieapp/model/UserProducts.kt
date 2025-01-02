package com.example.calorieapp.model

import com.google.firebase.firestore.DocumentId


enum class MealType {
    Breakfast,
    Lunch,
    Dinner,
    Snack
}

data class MealData(
    val mealName: MealType,
    val products: List<Product> = emptyList()
)

data class UserProducts(
    @DocumentId val id: String = "",
    val date: Long,
    val breakfast: MealData = MealData(mealName = MealType.Breakfast),
    val lunch: MealData = MealData(mealName = MealType.Lunch),
    val dinner: MealData = MealData(mealName = MealType.Dinner),
    val snacks: MealData = MealData(mealName = MealType.Snack)
)