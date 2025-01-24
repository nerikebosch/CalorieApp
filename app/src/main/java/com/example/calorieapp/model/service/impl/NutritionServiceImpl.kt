package com.example.calorieapp.model.service.impl

import com.example.calorieapp.model.MealData
import com.example.calorieapp.model.MealName
import com.example.calorieapp.model.User
import com.example.calorieapp.model.UserData
import com.example.calorieapp.model.UserProducts
import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.NutritionService
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class NutritionServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AccountService,
) : NutritionService {

    override suspend fun getTotalCalories(userProducts: UserProducts): Double {
        return listOf(userProducts.breakfast, userProducts.lunch,
            userProducts.dinner, userProducts.snacks)
            .sumOf { getMealCalories(it) }
    }

    override suspend fun getTotalProtein(userProducts: UserProducts): Double {
        return listOf(userProducts.breakfast, userProducts.lunch,
            userProducts.dinner, userProducts.snacks)
            .flatMap { it.products }
            .sumOf { product ->
                product.nutrients?.protein ?: 0.0
            }
    }

    override suspend fun getTotalCarbs(userProducts: UserProducts): Double {
        return listOf(userProducts.breakfast, userProducts.lunch,
            userProducts.dinner, userProducts.snacks)
            .flatMap { it.products }
            .sumOf { product ->
                product.nutrients?.carbohydrates ?: 0.0
            }
    }

    override suspend fun getTotalFat(userProducts: UserProducts): Double {
        return listOf(userProducts.breakfast, userProducts.lunch,
            userProducts.dinner, userProducts.snacks)
            .flatMap { it.products }
            .sumOf { product ->
                product.nutrients?.fat ?: 0.0
            }
    }

    override suspend fun getMealCalories(mealData: MealData): Double {
        return mealData.products.sumOf { product ->
            product.nutrients?.calories ?: 0.0
        }
    }

    override suspend fun getTotalCaloriesForDate(userProducts: List<UserProducts>, date: String): Double {
        return userProducts
            .find { it.date == date }
            ?.let { getTotalCalories(it) }
            ?: 0.0
    }

    override suspend fun getMealDataByType(userProducts: UserProducts, mealName: MealName): MealNutrientTotals {
        val mealData = when (mealName) {
            MealName.Breakfast -> userProducts.breakfast
            MealName.Lunch -> userProducts.lunch
            MealName.Dinner -> userProducts.dinner
            MealName.Snack -> userProducts.snacks
        }

        return MealNutrientTotals(
            totalCalories = mealData.products.sumOf { it.nutrients?.calories ?: 0.0 },
            totalProtein = mealData.products.sumOf { it.nutrients?.protein ?: 0.0 },
            totalCarbohydrates = mealData.products.sumOf { it.nutrients?.carbohydrates ?: 0.0 },
            totalFat = mealData.products.sumOf { it.nutrients?.fat ?: 0.0 }
        )
    }

    override suspend fun getMealNutrients(mealData: MealData): Triple<Double, Double, Double> {
        val proteins = mealData.products.sumOf { it.nutrients?.protein ?: 0.0 }
        val carbs = mealData.products.sumOf { it.nutrients?.carbohydrates ?: 0.0 }
        val fats = mealData.products.sumOf { it.nutrients?.fat ?: 0.0 }
        return Triple(proteins, carbs, fats)
    }

}


data class MealNutrientTotals(
    val totalCalories: Double = 0.0,
    val totalProtein: Double = 0.0,
    val totalCarbohydrates: Double = 0.0,
    val totalFat: Double = 0.0
)