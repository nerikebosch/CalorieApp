package com.example.calorieapp.screens.homescreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calorieapp.R
import com.example.calorieapp.common.composable.DialogCancelButton
import com.example.calorieapp.common.composable.DialogConfirmButton
import com.example.calorieapp.model.MealName
import com.example.calorieapp.model.User
import com.example.calorieapp.screens.recipe.MealSelectionDialog
import java.time.LocalDate
import kotlin.math.roundToInt

@Composable
fun GradientProgressIndicator(
    progress: Double,
    modifier: Modifier = Modifier,
    gradientStart: Color,
    gradientEnd: Color,
    trackColor: Color,
    strokeWidth: Dp
) {
    Canvas(modifier = modifier) {
        val size = size.minDimension // Ensure it's square
        val radius = (size / 2) - strokeWidth.toPx() / 2

        // Draw the track (background of the progress bar)
        drawCircle(
            color = trackColor,
            radius = radius,
            style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        )

        // Draw the gradient progress arc
        val sweepAngle = ( progress * 360.0f ).toFloat()
        val brush = Brush.linearGradient(
            colors = listOf(gradientStart, gradientEnd),
            start = Offset.Zero,
            end = Offset(size, size)
        )

        drawArc(
            brush = brush,
            startAngle = -90f,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        )
    }
}

@Composable
fun GradientProgressIndicatorWater(
    progress: Double,
    modifier: Modifier = Modifier,
    gradientStart: Color,
    gradientEnd: Color,
    trackColor: Color,
    strokeWidth: Dp
) {
    Canvas(modifier = modifier) {
        val size = size.minDimension // Ensure it's square
        val radius = (size / 2) - strokeWidth.toPx() / 2

        // Draw the track (background of the progress bar)
        drawCircle(
            color = trackColor,
            radius = radius,
            style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        )

        // Draw the gradient progress arc
        val sweepAngle = (progress * 360f).toFloat()
        val brush = Brush.linearGradient(
            colors = listOf(gradientStart, gradientEnd),
            start = Offset.Zero,
            end = Offset(size, size)
        )

        drawArc(
            brush = brush,
            startAngle = -90f,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        )
    }
}


@Composable
fun CalorieProgressIndicator(
    currentCalories: Double,
    goalCalories: Double,
    modifier: Modifier = Modifier,
    gradientStart: Color,
    gradientEnd: Color,
    trackColor: Color,
    strokeWidth: Dp
) {
    // Calculate progress as a ratio of current to goal
    val progress = (currentCalories.toDouble() / goalCalories)

    GradientProgressIndicator(
        progress = progress,
        modifier = modifier,
        gradientStart = gradientStart,
        gradientEnd = gradientEnd,
        trackColor = trackColor,
        strokeWidth = strokeWidth
    )
}

@Composable
fun WaterProgressIndicator(
    currentIntake: Double,
    goalIntake: Double,
    modifier: Modifier = Modifier,
    gradientStart: Color,
    gradientEnd: Color,
    trackColor: Color,
    strokeWidth: Dp
) {
    // Calculate progress as a ratio of current to goal
    val progress = (currentIntake / goalIntake)

    GradientProgressIndicatorWater(
        progress = progress,
        modifier = modifier,
        gradientStart = gradientStart,
        gradientEnd = gradientEnd,
        trackColor = trackColor,
        strokeWidth = strokeWidth
    )
}
/* ***************************************** */
@Composable
fun HomeScreen(
    openScreen: (String) -> Unit,
    viewModel: HomeScreenViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val user by viewModel.user.collectAsStateWithLifecycle()

    LaunchedEffect(true) { viewModel.onUserLoad() }
    var showDialog by remember { mutableStateOf(false) }
    var showWeightDialog by remember { mutableStateOf(false) }

    Column {
        HomeScreenContent(
            user = user,
            uiState = uiState,
            onSettingsClick = { viewModel.onSettingsClick(openScreen) },
            onOpenDialog = { showDialog = true },
            onOpenWeightDialog = { showWeightDialog = true }
        )

            ItemAddDialog(
                showDialog = showDialog,
                onDismiss = { showDialog = false },
                onSave = { addedAmount ->
                    viewModel.updateWaterIntake(addedAmount) }
            )

        ItemWeightDialog(
            showDialog = showWeightDialog,
            onDismiss = { showWeightDialog = false },
            onSave = { newWeight -> viewModel.updateUserWeight(newWeight) }
        )

    }
}

@Composable
fun HomeScreenContent(
    user: User,
    uiState: HomeScreenUiState,
    onSettingsClick: () -> Unit,
    onOpenDialog: () -> Unit,
    onOpenWeightDialog: () -> Unit
) {
    val date = LocalDate.now()

    Column(modifier = Modifier.padding(16.dp)) {

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "Hi, ${user.name}!",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )

            // Go to settings screen
            IconButton(
                onClick = { onSettingsClick() },
                modifier = Modifier.align(Alignment.CenterVertically)) {
                Icon(Icons.Filled.Menu, contentDescription = "Menu")
            }
        }

        Text(
            text = "$date",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        ElevatedCardCalorieTracker(
            title = "Today's Calories",
            currentCalories = uiState.currentCalorie,
            goalCalories = user.goalCalorie,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

            ElevatedCardWaterTracker(
                title = "Weight Balance",
                currentIntake = user.weight,
                goalIntake = user.goalWeight,
                unit = "kg",
                modifier = Modifier.size(width = 170.dp, height = 200.dp),
                onClick = { onOpenWeightDialog() }
            )
            //Spacer(modifier = Modifier.width(10.dp))

            ElevatedCardWaterTracker(
                title = "Water Balance",
                currentIntake = uiState.currentWater, // Replace with actual data
                goalIntake = user.goalWater,  // Replace with actual data
                unit = "ml",
                modifier = Modifier.size(width = 180.dp, height = 200.dp),
                onClick = { onOpenDialog() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        ElevatedCardHomeScreen(
            modifier = Modifier.fillMaxWidth(),
            title = uiState.mealTitle
        ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Text(
                        modifier = Modifier.padding(15.dp),
                        text = uiState.mealCalories.toString() + " calories",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(15.dp))

                    Row {
                        Spacer(modifier = Modifier.width(15.dp))
                        Column {
                            Text(
                                text = "Proteins",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = uiState.mealProteins.toString(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Spacer(modifier = Modifier.width(50.dp))
                        Column {

                            Text(
                                text = "Carbs",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = uiState.mealCarbs.toString(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Spacer(modifier = Modifier.width(50.dp))
                        Column{

                            Text(
                                text = "Fats",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = uiState.mealFats.toString(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Spacer(modifier = Modifier.width(50.dp))
                        Column {
                            Text(
                                text = "RDC",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = uiState.mealRDC.toString(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                }

        }

        Spacer(modifier = Modifier.height(16.dp))

    }
}

@Composable
fun ItemAddDialog(
    dialogData: String = "",
    uiState: HomeScreenUiState = HomeScreenUiState(),
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onSave: (Double) -> Unit
) {
    if (showDialog) {

        var sliderPosition by remember { mutableFloatStateOf(0f) }
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = {
                Text(stringResource(R.string.add_amount))
            },
            text = {
                Column {
                    Slider(
                        value = sliderPosition,
                        onValueChange = { newValue ->
                            // Snap to the nearest step
                            sliderPosition = (newValue / 10).roundToInt() * 10f
                        },
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.secondary,
                            activeTrackColor = MaterialTheme.colorScheme.secondary,
                            inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                        ),
                        steps = 200,
                        valueRange = 0f..2000f
                    )
                    Text(text = sliderPosition.toInt().toString())
                }
            },
            confirmButton = {
                DialogConfirmButton(R.string.confirm) {
                    onSave(sliderPosition.toDouble())
                    onDismiss()
                }
            },
            dismissButton = {
                DialogCancelButton(R.string.cancel) { onDismiss() }
            }
        )
    }


}

@Composable
fun ItemWeightDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onSave: (Double) -> Unit
) {
    if (showDialog) {
        var weightInput by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(stringResource(R.string.add_amount)) },
            text = {
                Column {
                    Text(text = "Enter your new weight (kg):")

                    TextField(
                        value = weightInput,
                        onValueChange = { newValue ->
                            // Allow only numbers and a decimal point
                            if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                                weightInput = newValue
                            }
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                DialogConfirmButton(R.string.confirm) {
                    weightInput.toDoubleOrNull()?.let { newWeight ->
                        onSave(newWeight)
                        onDismiss()
                    }
                }
            },
            dismissButton = {
                DialogCancelButton(R.string.cancel) { onDismiss() }
            }
        )
    }
}






@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val uiState = HomeScreenUiState(
        currentCalorie = 1200.0,
        currentWeight = 58.0,
        currentWater = 1230.0,

        mealTitle = "Breakfast",
        mealCalories = 1234.0,
        mealProteins = 60.0,
        mealCarbs = 100.0,
        mealFats = 20.0,
        mealRDC = 20.0,
    )
    val user = User(
        email = "nerike.b@gmail.com",
        name = "Nerike",
        surname = "Bosch",
        registeredUser = true,

        goalWeight = 50.0,
        goalWater = 2000.0,
        goalCalorie = 2000.0
    )
    HomeScreenContent(
        user = user,
        uiState = uiState,
        onSettingsClick = { },
        onOpenDialog = { },
        onOpenWeightDialog = { }
    )

}
