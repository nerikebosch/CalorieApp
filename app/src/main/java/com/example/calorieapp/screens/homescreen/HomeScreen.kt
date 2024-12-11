package com.example.calorieapp.screens.homescreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.calorieapp.R
import com.example.calorieapp.firebase.User
import com.google.firebase.auth.FirebaseUser

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
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier.fillMaxSize(),
    currentUser: FirebaseUser?,
    onSignOutClick: () -> Unit
) {
    val name: String = User().name
    val navController = rememberNavController()

    Column(modifier = Modifier.padding(16.dp)) {
        currentUser?.let { user ->
            user.displayName?.let { firstName ->
                Text(
                    text = "Hello $firstName",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ElevatedCardCalorieTracker(
            title = "Today's Calories",
            currentCalories = 1200, // Replace with actual data
            goalCalories = 2000,  // Replace with actual data
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            ElevatedCardHomeScreen(
                title = "Weight",
                modifier = Modifier.weight(1f) // Each card takes up equal space
            )
            Spacer(modifier = Modifier.width(16.dp))
            ElevatedCardHomeScreen(
                title = "Water balance",
                modifier = Modifier.weight(1f)
            ) {
                // Add vector image here inside the ElevatedCard
                val waterGlassImage = painterResource(id = R.drawable.water_glass)

                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Display the vector image
                    Icon(
                        painter = waterGlassImage,
                        contentDescription = "Water Glass",
                        modifier = Modifier.size(48.dp) // Adjust size as needed
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Space between icon and text
                    // Optionally, add text or other components here
                    Text(
                        text = "Water Balance",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ElevatedCardHomeScreen(
            modifier = Modifier.fillMaxWidth(),
            title = "Breakfast"
        )

        Button(onClick = { onSignOutClick() }) {
            Text(
                text = "Sign out",
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TabRowExample()
    }
}


@Composable
fun TabRowExample() {
    var state by remember { mutableStateOf(0) }
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
    val navController = rememberNavController()
    HomeScreen(navController = navController, currentUser = null, onSignOutClick = {},

        )
}
