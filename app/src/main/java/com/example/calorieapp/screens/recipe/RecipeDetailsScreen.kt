package com.example.calorieapp.screens.recipe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calorieapp.model.NutritionalValues
import com.example.calorieapp.model.RecipeDetails

@Composable
fun RecipeDetailsScreen(
    recipeName: String, // Receive recipe name
    openAndPopUp: (String, String) -> Unit,
    viewModel: RecipeDetailsViewModel = hiltViewModel() // Inject ViewModel
    ) {
    LaunchedEffect(recipeName) {
        viewModel.loadRecipe(recipeName) // Fetch recipe details
    }

        // Find the selected recipe by its name
    val selectedRecipe by viewModel.selectedRecipe.collectAsState()

    selectedRecipe?.let { recipe ->
        RecipeDetailsSelection(recipe = recipe)
    }
    }

@Composable
fun RecipeDetailsSelection(
    recipe: RecipeDetails
){
    Surface(
        modifier = Modifier.fillMaxSize()
    ){
        LazyColumn {
            item {
                ElevatedCardRecipeScreen(
                    title = recipe.name,
                    img = recipe.imageUrl
                )
            }
            item {
                NutritionalInfoCard(recipe.nutritionalValues)
            }
            item {
                IngredientsCard(recipe.ingredients)
            }
            item {
                InstructionsCard(recipe.instructions)
            }
        }
    }
}

@Composable
fun NutritionalInfoCard(nutritionalValues: NutritionalValues) {
    ElevatedCardRecipeDetails(
        title = "Nutritional Information",
        content = {
            Column {
                Text("Calories: ${nutritionalValues.calories}")
                Text("Fat: ${nutritionalValues.fat}g")
                Text("Protein: ${nutritionalValues.protein}g")
                Text("Carbohydrates: ${nutritionalValues.carbohydrates}g")
            }
        }
    )
}


@Composable
fun IngredientsCard(ingredients: List<String>) {
    ElevatedCardRecipeDetails(
        title = "Ingredients",
        content = {
            Column {
                ingredients.forEach { ingredient ->
                    Text("• $ingredient")
                }
            }
        }
    )
}


@Composable
fun InstructionsCard(instructions: List<String>) {
    ElevatedCardRecipeDetails(
        title = "Instructions",
        content = {
            Column {
                instructions.forEachIndexed { index, step ->
                    Text("${index + 1}. $step")
                }
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun RecipeDetailsPreview(){
    RecipeDetailsSelection(
        recipe = RecipeDetails(
            name = "Recipe Name",
            category = "Category",
            imageUrl = "Image URL",
            nutritionalValues = NutritionalValues(
                calories = 100,
                fat = 10,
                protein = 20,
                carbohydrates = 70,
                ),
            ingredients = listOf("Ingredient 1", "Ingredient 2", "Ingredient 3"),
            instructions = listOf("Step 1", "Step 2", "Step 3")
        )
    )
}