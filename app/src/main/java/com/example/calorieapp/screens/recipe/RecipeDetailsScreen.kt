package com.example.calorieapp.screens.recipe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calorieapp.R as AppText
import com.example.calorieapp.common.composable.TextActionToolbar
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
    recipe: RecipeDetails,
    modifier: Modifier = Modifier
){
    Surface(
        modifier = Modifier.fillMaxSize()
    ){

        Column {
            TextActionToolbar(
                title = AppText.string.select_recipe,
                text = "Save",
                modifier = Modifier,
                endAction = {  }
            )

            Spacer(modifier = Modifier.height(15.dp))

            LazyColumn {

                item {
                    ElevatedCardRecipeScreen(
                        title = recipe.name,
                        img = recipe.imageUrl
                    )
                }


                item {
                    Spacer(modifier = Modifier.height(15.dp))
                    NutritionalInfoCard(recipe.nutritionalValues)
                }
                item {
                    Spacer(modifier = Modifier.height(15.dp))
                    IngredientsCard(recipe.ingredients)
                }
                item {
                    Spacer(modifier = Modifier.height(15.dp))
                    InstructionsCard(recipe.instructions)
                }
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
                Spacer(modifier = Modifier.height(8.dp))
                Text("Calories: ${nutritionalValues.calories}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Fat: ${nutritionalValues.fat}g")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Protein: ${nutritionalValues.protein}g")
                Spacer(modifier = Modifier.height(8.dp))
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
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• $ingredient")
                    Spacer(modifier = Modifier.height(8.dp))
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
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("${index + 1}. $step")
                    Spacer(modifier = Modifier.height(8.dp))
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