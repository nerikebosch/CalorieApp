package com.example.calorieapp.screens.recipe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calorieapp.common.composable.DialogCancelButton
import com.example.calorieapp.common.composable.DialogConfirmButton
import com.example.calorieapp.common.composable.TextActionToolbar
import com.example.calorieapp.model.MealName
import com.example.calorieapp.model.NutritionalValues
import com.example.calorieapp.model.RecipeDetails
import com.example.calorieapp.R.string as AppText


/**
 * Composable function that displays the details of a selected recipe.
 *
 * @param recipeName The name of the recipe to display.
 * @param popUpScreen A lambda function to handle navigation back.
 * @param viewModel The ViewModel responsible for fetching and managing recipe details.
 */
@ExperimentalMaterial3Api
@Composable
fun RecipeDetailsScreen(
    recipeName: String, // Receive recipe name
    popUpScreen: () -> Unit,
    viewModel: RecipeDetailsViewModel = hiltViewModel() // Inject ViewModel
) {
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(recipeName) {
        viewModel.loadRecipe(recipeName) // Fetch recipe details
    }
        // Find the selected recipe by its name
    val selectedRecipe by viewModel.selectedRecipe.collectAsState()

    selectedRecipe?.let { recipe ->
        RecipeDetailsSelection(
            recipe = recipe,
            onOpenDialog = { showDialog = true }
        )

        MealSelectionDialog(
            showDialog = showDialog,
            onDismiss = { showDialog = false },
            onSave = { selectedMeals ->
                viewModel.saveToMeal(selectedMeals, recipe)
                showDialog = false
            }
        )
    }
}


/**
 * Displays detailed information about a recipe, including its name, image, nutritional values,
 * ingredients, and instructions.
 *
 * @param recipe The recipe details to display.
 * @param onOpenDialog Lambda function triggered when the "Add" button is clicked.
 */
@Composable
fun RecipeDetailsSelection(
    recipe: RecipeDetails,
    onOpenDialog: () -> Unit
){
    Surface(
        modifier = Modifier.fillMaxSize()
    ){

        Column {
            TextActionToolbar(
                title = AppText.select_recipe,
                text = "Add",
                modifier = Modifier,
                endAction = { onOpenDialog() }
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


/**
 * Displays nutritional information of a recipe.
 *
 * @param nutritionalValues The nutritional values including calories, fat, protein, and carbs.
 */
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


/**
 * Displays the list of ingredients required for the recipe.
 *
 * @param ingredients List of ingredients for the recipe.
 */
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


/**
 * Displays step-by-step instructions for preparing the recipe.
 *
 * @param instructions List of instructions for the recipe.
 */
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


/**
 * Displays a dialog for selecting which meal to add the recipe to.
 *
 * @param showDialog Boolean indicating whether the dialog should be shown.
 * @param onDismiss Callback when the dialog is dismissed.
 * @param onSave Callback when the user confirms meal selection.
 */
@Composable
fun MealSelectionDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onSave: (List<String>) -> Unit
) {
    if (showDialog) {
        var selectedMeals by remember { mutableStateOf(setOf<String>()) }

        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = {
                Text(stringResource(AppText.add_to_meal_today))
            },
            text = {
                Column {
                    // List of meal names with checkboxes
                    MealName.entries.forEach { meal ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = selectedMeals.contains(meal.name),
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        selectedMeals += meal.name
                                    } else {
                                        selectedMeals -= meal.name
                                    }
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = MaterialTheme.colorScheme.primary,
                                    uncheckedColor = MaterialTheme.colorScheme.onSurface,
                                    checkmarkColor = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                            Text(
                                text = meal.name,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                    }
                }
            },
            confirmButton = {
                DialogConfirmButton(AppText.confirm) {
                    onSave(selectedMeals.toList())
                    onDismiss()
                }
            },
            dismissButton = {
                DialogCancelButton(AppText.cancel) { onDismiss() }
            }
        )
    }
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
                calories = 100.0,
                fat = 10.0,
                protein = 20.0,
                carbohydrates = 70.0,
                ),
            ingredients = listOf("Ingredient 1", "Ingredient 2", "Ingredient 3", "Ingredient 4"),
            instructions = listOf("Step 1", "Step 2", "Step 3")
        ),
        onOpenDialog = {}
    )
}
