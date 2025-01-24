package com.example.calorieapp.model.service

import com.example.calorieapp.model.MealData
import com.example.calorieapp.model.MealName
import com.example.calorieapp.model.UserProducts
import com.example.calorieapp.model.service.impl.MealNutrientTotals

interface NutritionService {

    suspend fun getTotalCalories(userProducts: UserProducts): Double
    suspend fun getTotalProtein(userProducts: UserProducts): Double
    suspend fun getTotalCarbs(userProducts: UserProducts): Double
    suspend fun getTotalFat(userProducts: UserProducts): Double
    suspend fun getMealCalories(mealData: MealData): Double
    suspend fun getTotalCaloriesForDate(userProducts: List<UserProducts>, date: String): Double
    suspend fun getMealDataByType(userProducts: UserProducts, mealName: MealName): MealNutrientTotals
    suspend fun getMealNutrients(mealData: MealData): Triple<Double, Double, Double>

}