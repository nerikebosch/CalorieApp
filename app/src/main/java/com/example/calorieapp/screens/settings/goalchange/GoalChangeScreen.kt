package com.example.calorieapp.screens.settings.goalchange


import android.R.attr.label
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.calorieapp.common.composable.LabelNumberTextField
import com.example.calorieapp.common.composable.TextActionToolbar
import com.example.calorieapp.common.ext.spacer
import com.example.calorieapp.common.ext.textCard
import com.example.calorieapp.common.ext.toolbarActions
import com.example.calorieapp.model.User
import com.example.calorieapp.ui.theme.CalorieAppTheme
import com.example.calorieapp.R.string as AppText


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
        user = user,
        onGoalWaterChange = viewModel::onGoalWaterChange,
        onGoalWeightChange = viewModel::onGoalWeightChange,
        onGoalCalorieChange = viewModel::onGoalCalorieChange
    )

}

@Composable
fun GoalChangeScreenContent(
    onDoneClick: () -> Unit = {},
    user: User,
    onGoalWaterChange: (String) -> Unit = {},
    onGoalWeightChange: (String) -> Unit = {},
    onGoalCalorieChange: (String) -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight().verticalScroll(rememberScrollState()),
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
            value = user.goalWeight.toString(),
            onValueChange = onGoalWeightChange,
            label = "Change your goal weight",
            modifier = Modifier.textCard(),
            trailing = { Text(text = "kg") }
        )
        Spacer(modifier = Modifier.spacer())
        LabelNumberTextField(
            value = user.goalWater.toString(),
            onValueChange = onGoalWaterChange,
            label = "Change your daily goal water",
            modifier = Modifier.textCard(),
            trailing = { Text(text = "ml") }
        )
        Spacer(modifier = Modifier.spacer())
        LabelNumberTextField(
            value = user.goalCalorie.toString(),
            onValueChange = onGoalCalorieChange,
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
            goalWater = 2000.0, goalWeight = 60.0, goalCalorie = 2000.0
        )) }

        GoalChangeScreenContent(
            onDoneClick = {},
            user = previewUser,
            onGoalWaterChange = { previewUser = previewUser.copy(goalWater = it.toDouble()) },
            onGoalWeightChange = { previewUser = previewUser.copy(goalWeight = it.toDouble()) },
            onGoalCalorieChange = { previewUser = previewUser.copy(goalCalorie = it.toDouble()) }
        )
    }
}

