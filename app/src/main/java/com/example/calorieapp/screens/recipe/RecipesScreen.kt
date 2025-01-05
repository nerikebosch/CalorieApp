package com.example.calorieapp.screens.recipe

import android.content.Context
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calorieapp.model.NutritionalValues
import com.example.calorieapp.model.RecipeDetails



@Composable
fun RecipesScreen(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RecipesViewModel = hiltViewModel()
){
    val recipes by viewModel.recipes.observeAsState(emptyList())

    RecipeSelection(recipes = recipes)
}


@Composable
fun RecipeSelection(
    recipes: List<RecipeDetails>
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn {
            item {
                // Filter chips for categories
                Row {
                    FilterChip(title = "Breakfast")
                    Spacer(modifier = Modifier.width(15.dp))
                    FilterChip(title = "Lunch")
                    Spacer(modifier = Modifier.width(15.dp))
                    FilterChip(title = "Dinner")
                    Spacer(modifier = Modifier.width(15.dp))
                    FilterChip(title = "Snacks")
                }

                Spacer(modifier = Modifier.height(15.dp))
            }

            // Display recipes as ElevatedCardRecipeScreen
            items(recipes) { recipe ->
                ElevatedCardRecipeScreen(
                    title = recipe.name,  // Ensure property matches RecipeDetails data class
                    img = recipe.imageUrl // Ensure property matches RecipeDetails data class
                )
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RecipePreview() {
    val dummyRecipes = getDummyRecipes() // Update this function if necessary

    Surface(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            // Show dummy recipes
            items(dummyRecipes) { recipe ->
                ElevatedCardRecipeScreen(
                    title = recipe.name,  // Access name directly from RecipeDetails
                    img = recipe.imageUrl // Access imageUrl directly from RecipeDetails
                )
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}

fun getDummyRecipes(): List<RecipeDetails> {
    return listOf(
        RecipeDetails(
            category = "Breakfast",
            name = "Avocado Toast",
            imageUrl = "https://www.eatingwell.com/thmb/PM3UlLhM0VbE6dcq9ZFwCnMyWHI=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/EatingWell-April-Avocado-Toast-Directions-04-5b5b86524a3d4b35ac4c57863f6095dc.jpg",
            nutritionalValues = NutritionalValues(
                calories = 250,
                fat = 14,
                protein = 10,
                carbohydrates = 22
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
                calories = 180,
                fat = 6,
                protein = 12,
                carbohydrates = 25
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