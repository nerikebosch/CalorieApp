package com.example.calorieapp.screens.recipe

import android.content.Context
import com.example.calorieapp.model.MealName
import com.example.calorieapp.model.RecipeDetails
import com.example.calorieapp.model.UserProducts
import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.LogService
import com.example.calorieapp.model.service.StorageService
import com.example.calorieapp.model.toProduct
import com.example.calorieapp.screens.CalorieAppViewModel
import com.example.calorieapp.screens.adddata.formatDateToString
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val recipeRepository: RecipeRepository, // Fetches recipes
    logService: LogService,
    private val accountService: AccountService,
    private val storageService: StorageService,
) : CalorieAppViewModel(logService) {

    private val _selectedRecipe = MutableStateFlow<RecipeDetails?>(null)
    val selectedRecipe: StateFlow<RecipeDetails?> get() = _selectedRecipe

    fun loadRecipe(recipeName: String) {
        launchCatching {
            val recipe = recipeRepository.getRecipeByName(recipeName)
            _selectedRecipe.value = recipe
        }
    }
    fun saveToMeal(selectedMeals: List<String>, recipe: RecipeDetails) {
        val today = formatDateToString(Calendar.getInstance().timeInMillis)
        val product = recipe.toProduct()

        launchCatching {
            // Get or create UserProducts for today
            var userProducts = storageService.getUserProductByDate(today) ?: UserProducts(date = today)

            // Add the product to each selected meal
            selectedMeals.forEach { mealName ->
                when (MealName.valueOf(mealName)) {
                    MealName.Breakfast -> userProducts = userProducts.copy(
                        breakfast = userProducts.breakfast.copy(
                            products = userProducts.breakfast.products + product
                        )
                    )
                    MealName.Lunch -> userProducts = userProducts.copy(
                        lunch = userProducts.lunch.copy(
                            products = userProducts.lunch.products + product
                        )
                    )
                    MealName.Dinner -> userProducts = userProducts.copy(
                        dinner = userProducts.dinner.copy(
                            products = userProducts.dinner.products + product
                        )
                    )
                    MealName.Snack -> userProducts = userProducts.copy(
                        snacks = userProducts.snacks.copy(
                            products = userProducts.snacks.products + product
                        )
                    )
                }
            }

            // Save or update the userProducts
            if (userProducts.id.isEmpty()) {
                storageService.saveUserProduct(userProducts)
            } else {
                storageService.updateUserProduct(userProducts)
            }
        }
    }
}
