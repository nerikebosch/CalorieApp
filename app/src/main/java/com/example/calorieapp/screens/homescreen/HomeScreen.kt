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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlin.Int

@Composable
fun GradientProgressIndicator(
    progress: Float,
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
        val sweepAngle = progress * 360f
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
    progress: Float,
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
        val sweepAngle = progress * 360f
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
    currentCalories: Int,
    goalCalories: Int,
    modifier: Modifier = Modifier,
    gradientStart: Color,
    gradientEnd: Color,
    trackColor: Color,
    strokeWidth: Dp
) {
    // Calculate progress as a ratio of current to goal
    val progress = (currentCalories.toFloat() / goalCalories).coerceIn(0f, 1f)

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
    currentIntake: Float,
    goalIntake: Float,
    modifier: Modifier = Modifier,
    gradientStart: Color,
    gradientEnd: Color,
    trackColor: Color,
    strokeWidth: Dp
) {
    // Calculate progress as a ratio of current to goal
    val progress = (currentIntake.toFloat() / goalIntake).coerceIn(0f, 1f)

    GradientProgressIndicatorWater(
        progress = progress,
        modifier = modifier,
        gradientStart = gradientStart,
        gradientEnd = gradientEnd,
        trackColor = trackColor,
        strokeWidth = strokeWidth
    )
}

@Composable
fun HomeScreen(
    openScreen: (String) -> Unit,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState

    HomeScreenContent(
        uiState = uiState,
        onSettingsClick = { viewModel.onSettingsClick(openScreen) },
    )
}

@Composable
fun HomeScreenContent(
    uiState: HomeScreenUiState,
    onSettingsClick: () -> Unit,
) {
    val date = java.time.LocalDate.now()

    Column(modifier = Modifier.padding(16.dp)) {

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "Summary",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .align(Alignment.CenterVertically)

            )

            IconButton(
                onClick = { onSettingsClick() },
                modifier = Modifier.align(Alignment.CenterVertically)) {
                Icon(Icons.Filled.Menu, contentDescription = "Menu")
            }

//            Image(
//                painter = painterResource(id = R.drawable.profile),
//                contentDescription = null,
//                modifier = Modifier
//                    .size(60.dp)
//                    .align(Alignment.CenterVertically)
//            )


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
            currentCalories = uiState.currentCalorie, // Replace with actual data
            goalCalories = uiState.goalCalorie,  // Replace with actual data
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

            ElevatedCardWaterTracker(
                title = "Weight Balance",
                currentIntake = uiState.currentWeight, // Replace with actual data
                goalIntake = uiState.goalWeight,  // Replace with actual data
                unit = "kg",
                modifier = Modifier.size(width = 170.dp, height = 200.dp)
            )
            //Spacer(modifier = Modifier.width(10.dp))

            ElevatedCardWaterTracker(
                title = "Water Balance",
                currentIntake = uiState.currentWater, // Replace with actual data
                goalIntake = uiState.goalWater,  // Replace with actual data
                unit = "ml",
                modifier = Modifier.size(width = 180.dp, height = 200.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        ElevatedCardHomeScreen(
            modifier = Modifier.fillMaxWidth(),
            title = "Breakfast"
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

                    Row() {
                        Spacer(modifier = Modifier.width(15.dp))
                        Column() {
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
                        Column() {

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
                        Column(){

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
                        Column() {
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

        TabRowExample()
    }
}


@Composable
fun TabRowExample() {
    var state by remember { mutableIntStateOf(0) }
    val titles = listOf("Recipes", "Add data", "Statistics")

    Column {
        TabRow(selectedTabIndex = state) {
            titles.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = (index == state),
                    onClick = { state = index }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TabRowExamplePreview() {
    val uiState = HomeScreenUiState(
        currentCalorie = 1200,
        goalCalorie = 2500,
        currentWeight = 78.0f,
        goalWeight = 70.0f,
        currentWater = 1230.0f,
        goalWater = 2000.0f,

        mealTitle = "Breakfast",
        mealCalories = 1234,
        mealProteins = 60,
        mealCarbs = 100,
        mealFats = 20,
        mealRDC = 20,
    )
    HomeScreenContent(
        uiState = uiState,
        onSettingsClick = { },
    )
}
