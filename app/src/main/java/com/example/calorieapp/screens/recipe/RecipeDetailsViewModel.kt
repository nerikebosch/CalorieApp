package com.example.calorieapp.screens.recipe

import android.content.Context
import com.example.calorieapp.R.string as AppText
import com.example.calorieapp.common.snackbar.SnackbarManager
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

/**
 * ViewModel responsible for managing the recipe details screen.
 * It handles fetching recipe details and saving selected recipes to meals.
 *
 * @property context Application context for accessing resources.
 * @property recipeRepository Repository to fetch recipe details.
 * @property accountService Service for user account management.
 * @property storageService Service for managing stored user products.
 * @property logService Service for logging errors and events.
 */
@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val recipeRepository: RecipeRepository, // Fetches recipes
    logService: LogService,
    private val accountService: AccountService,
    private val storageService: StorageService,
) : CalorieAppViewModel(logService) {

    /**
     * Holds the currently selected recipe details.
     */
    private val _selectedRecipe = MutableStateFlow<RecipeDetails?>(null)

    /**
     * Public immutable access to the selected recipe.
     */
    val selectedRecipe: StateFlow<RecipeDetails?> get() = _selectedRecipe

    /**
     * Loads a recipe by its name from the repository and updates [_selectedRecipe].
     *
     * @param recipeName The name of the recipe to fetch.
     */
    fun loadRecipe(recipeName: String) {
        launchCatching {
            val recipe = recipeRepository.getRecipeByName(recipeName)
            _selectedRecipe.value = recipe
        }
    }

    /**
     * Saves the selected recipe as a meal for the user on the current date.
     * The recipe is added to the selected meal categories (e.g., Breakfast, Lunch, etc.).
     *
     * @param selectedMeals List of meal names to which the recipe should be added.
     * @param recipe The recipe details to be saved.
     */
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
            SnackbarManager.showMessage(AppText.added_meal)
        }
    }

}
