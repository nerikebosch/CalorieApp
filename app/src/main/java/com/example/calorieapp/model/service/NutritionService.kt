package com.example.calorieapp.model.service

import com.example.calorieapp.model.MealData
import com.example.calorieapp.model.MealName
import com.example.calorieapp.model.UserProducts
import com.example.calorieapp.model.service.impl.MealNutrientTotals

interface NutritionService {

    fun getTotalCalories(userProducts: UserProducts): Double
    fun getTotalProtein(userProducts: UserProducts): Double
    fun getTotalCarbs(userProducts: UserProducts): Double
    fun getTotalFat(userProducts: UserProducts): Double
    fun getMealCalories(mealData: MealData): Double
    fun getTotalCaloriesForDate(userProducts: List<UserProducts>, date: String): Double
    fun getMealDataByType(userProducts: UserProducts, mealName: MealName): MealNutrientTotals
    fun getMealNutrients(mealData: MealData): Triple<Double, Double, Double>

}