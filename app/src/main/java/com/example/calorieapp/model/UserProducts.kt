package com.example.calorieapp.model

data class MealData(
    val mealName: String,
    val products: List<Product> = emptyList()
)

data class UserProducts(
    val breakfast: MealData = MealData(mealName = "Breakfast"),
    val lunch: MealData = MealData(mealName = "Lunch"),
    val dinner: MealData = MealData(mealName = "Dinner"),
    val snacks: MealData = MealData(mealName = "Snack")
)