package com.example.calorieapp.screens.recipe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calorieapp.model.NutritionalValues
import com.example.calorieapp.model.RecipeDetails



/**
 * Composable function for displaying the Recipes screen.
 *
 * @param openAndPopUp Function to navigate to a new screen and pop up the current screen.
 * @param viewModel ViewModel to fetch and manage recipe data.
 */
@Composable
fun RecipesScreen(
    openAndPopUp: (String, String) -> Unit,
    viewModel: RecipesViewModel = hiltViewModel()
){
    val recipes by viewModel.recipes.observeAsState(emptyList())

    RecipeSelection(
        recipes = recipes,
        onRecipeClick = { recipeName ->
            viewModel.onRecipeClick(openAndPopUp, recipeName)
        }
    )
}



/**
 * Displays a list of recipes with a category filter.
 *
 * @param recipes List of recipes to display.
 * @param onRecipeClick Callback invoked when a recipe is clicked.
 */
@Composable
fun RecipeSelection(
    recipes: List<RecipeDetails>,
    onRecipeClick: (String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn {
            item {
                // Filter chips for categories
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    val categories = listOf("Breakfast", "Lunch", "Dinner", "Snack")

                    categories.forEach { category ->
                        FilterChip(
                            title = category,
                            selectedCategory = selectedCategory,
                            onCategorySelected = { selectedCategory = it }
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))
            }
            // Filter recipes based on selected category
            val filteredRecipes = if (selectedCategory == null) recipes
            else recipes.filter { it.category == selectedCategory }

            // Display recipes as ElevatedCardRecipeScreen
            items(filteredRecipes) { recipe ->
                ElevatedCardRecipeScreen(
                    title = recipe.name,
                    img = recipe.imageUrl,
                    onClick = { onRecipeClick(recipe.name) }
                )
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}


/**
 * Preview function for displaying dummy recipe data in the UI.
 */
@Preview(showBackground = true)
@Composable
fun RecipePreview() {
    RecipeSelection(recipes = getDummyRecipes(),
        onRecipeClick = {})
}

/**
 * Generates a list of dummy recipes for preview purposes.
 *
 * @return List of dummy recipe details.
 */
fun getDummyRecipes(): List<RecipeDetails> {
    return listOf(
        RecipeDetails(
            category = "Breakfast",
            name = "Avocado Toast",
            imageUrl = "https://www.eatingwell.com/thmb/PM3UlLhM0VbE6dcq9ZFwCnMyWHI=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/EatingWell-April-Avocado-Toast-Directions-04-5b5b86524a3d4b35ac4c57863f6095dc.jpg",
            nutritionalValues = NutritionalValues(
                calories = 250.0,
                fat = 14.0,
                protein = 10.0,
                carbohydrates = 22.0
            ),
            ingredients = listOf("1 slice whole-grain bread", "1/2 avocado", "1 egg", "Salt and pepper to taste"),
            instructions = listOf(
                "Toast the bread until golden brown.",
                "Mash the avocado and spread it onto the toast.",
                "Fry the egg to your preference (sunny-side up or over easy).",
                "Place the egg on top of the avocado toast and season with salt and pepper."
            )
        ),
        RecipeDetails(
            category = "Breakfast",
            name = "Greek Yogurt Parfait",
            imageUrl = "https://www.eatingwell.com/thmb/QRX1MZetYhz1PfDCV--srer1Bts=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/250955-strawberry-yogurt-parfait-beauty-336980c8b011478b86d8b9450be09a32.jpg",
            nutritionalValues = NutritionalValues(
                calories = 180.0,
                fat = 6.0,
                protein = 12.0,
                carbohydrates = 25.0
            ),
            ingredients = listOf("1 cup Greek yogurt", "1/2 cup berries", "1 tbsp honey", "1/4 cup granola"),
            instructions = listOf(
                "Layer Greek yogurt, berries, honey, and granola.",
                "Repeat layers as desired.",
                "Serve immediately."
            )
        ),
        RecipeDetails(
            category = "Lunch",
            name = "Greek Yogurt Parfait",
            imageUrl = "https://www.eatingwell.com/thmb/QRX1MZetYhz1PfDCV--srer1Bts=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/250955-strawberry-yogurt-parfait-beauty-336980c8b011478b86d8b9450be09a32.jpg",
            nutritionalValues = NutritionalValues(
                calories = 180.0,
                fat = 6.0,
                protein = 12.0,
                carbohydrates = 25.0
            ),
            ingredients = listOf("1 cup Greek yogurt", "1/2 cup berries", "1 tbsp honey", "1/4 cup granola"),
            instructions = listOf(
                "Layer Greek yogurt, berries, honey, and granola.",
                "Repeat layers as desired.",
                "Serve immediately."
            )
        )
        

        // Add more dummy recipes here as needed
    )
}