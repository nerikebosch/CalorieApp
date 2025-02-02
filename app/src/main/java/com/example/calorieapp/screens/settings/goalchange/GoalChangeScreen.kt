package com.example.calorieapp.screens.settings.goalchange


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.calorieapp.common.composable.LabelNumberTextField
import com.example.calorieapp.common.composable.TextActionToolbar
import com.example.calorieapp.common.ext.spacer
import com.example.calorieapp.common.ext.textCard
import com.example.calorieapp.common.ext.toolbarActions
import com.example.calorieapp.model.User
import com.example.calorieapp.ui.theme.CalorieAppTheme
import java.text.DecimalFormat
import com.example.calorieapp.R.string as AppText


/**
 * Composable function that displays the Goal Change screen, allowing users to update their
 * personal goals for weight, water intake, and calorie consumption.
 *
 * @param popUpScreen Callback function to navigate back after saving goals.
 * @param viewModel ViewModel for handling user data and goal updates.
 */
@Composable
fun GoalChangeScreen(
    popUpScreen: () -> Unit,
    viewModel: GoalChangeViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsStateWithLifecycle()

    println("GoalChangeScreenDebug: Screen loaded")
    println("GoalChangeScreenDebug: User ID: ${user.id}, Goal Water: ${user.goalWater}, Goal Weight: ${user.goalWeight}, Goal Calorie: ${user.goalCalorie}")

    GoalChangeScreenContent(
        onDoneClick = { viewModel.onDoneClick(popUpScreen) },
        onGoalWaterChange = viewModel::onGoalWaterChange,
        onGoalWeightChange = viewModel::onGoalWeightChange,
        onGoalCalorieChange = viewModel::onGoalCalorieChange,
        user = user
    )

}


/**
 * Composable function that defines the UI layout for changing user goals.
 *
 * @param onGoalWaterChange Callback function for updating water intake goal.
 * @param onGoalWeightChange Callback function for updating weight goal.
 * @param onGoalCalorieChange Callback function for updating calorie goal.
 * @param onDoneClick Callback function triggered when the user saves changes.
 * @param user The current user object containing goal data.
 */
@Composable
fun GoalChangeScreenContent(
    onGoalWaterChange: (Double) -> Unit = {},
    onGoalWeightChange: (Double) -> Unit = {},
    onGoalCalorieChange: (Double) -> Unit = {},
    onDoneClick: () -> Unit = {},
    user: User,
) {
    var goalWeight by remember(user.id) { mutableStateOf(user.goalWeight) }
    var goalWater by remember(user.id)  { mutableStateOf(user.goalWater) }
    var goalCalorie by remember(user.id)  { mutableStateOf(user.goalCalorie) }
    val decimalFormat = remember { DecimalFormat("#.##") }



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            TextActionToolbar(
                title = AppText.goal_change,
                text = "Save",
                modifier = Modifier.toolbarActions(),
                endAction = onDoneClick
            )
        }
        Spacer(modifier = Modifier.spacer())

        LabelNumberTextField(
            value = if (goalWeight == 0.0) "" else {
                val formatted = decimalFormat.format(goalWeight)
                if (formatted.endsWith(".00")) formatted.substringBefore(".00")
                else if (formatted.endsWith(".0")) formatted.substringBefore(".0")
                else formatted
            },
            onValueChange = { newValue -> goalWeight = newValue.toDoubleOrNull() ?: 0.0
                onGoalWeightChange(goalWeight) },
            label = "Change your goal weight",
            modifier = Modifier.textCard(),
            trailing = { Text(text = "kg") }
        )
        Spacer(modifier = Modifier.spacer())
        LabelNumberTextField(
            value = if (goalWater == 0.0) "" else {
                val formatted = decimalFormat.format(goalWater)
                if (formatted.endsWith(".00")) formatted.substringBefore(".00")
                else if (formatted.endsWith(".0")) formatted.substringBefore(".0")
                else formatted
            },
            onValueChange = { newValue -> goalWater = newValue.toDoubleOrNull() ?: 0.0
                            onGoalWaterChange(goalWater) },
            label = "Change your daily goal water",
            modifier = Modifier.textCard(),
            trailing = { Text(text = "ml") }
        )
        Spacer(modifier = Modifier.spacer())
        LabelNumberTextField(
            value = if (goalCalorie == 0.0) "" else {
                val formatted = decimalFormat.format(goalCalorie)
                if (formatted.endsWith(".00")) formatted.substringBefore(".00")
                else if (formatted.endsWith(".0")) formatted.substringBefore(".0")
                else formatted
            },
            onValueChange = { newValue -> goalCalorie = newValue.toDoubleOrNull() ?: 0.0
                            onGoalCalorieChange(goalCalorie) },
            label = "Change your daily goal calorie",
            modifier = Modifier.textCard(),
            trailing = { Text(text = "kcal") }
        )
        Spacer(modifier = Modifier.spacer())
    }
}


@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun GoalChangeScreenPreview() {
    CalorieAppTheme {
        var previewUser by remember { mutableStateOf(User(
            goalWater = 0.0, goalWeight = 60.0, goalCalorie = 2000.0
        )) }

        GoalChangeScreenContent(
            onDoneClick = {},
            user = previewUser,
        )
    }
}

