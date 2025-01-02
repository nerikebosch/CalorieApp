package com.example.calorieapp.screens.homescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ElevatedCardHomeScreen(
    title: String, // Title of the card
    modifier: Modifier = Modifier, // Custom modifier for layout
    content: @Composable (() -> Unit)? = null // Lambda that can accept other composables (like icons, images)
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = modifier
            .size(width = 300.dp, height = 200.dp) // Adjust size as necessary
    ) {
        // Using Column for stacking items vertically (text + icon/content)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp), // Padding inside the card
            verticalArrangement = Arrangement.Center, // Center content vertically
            horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
        ) {
            // Title text at the top
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp) // Space between title and content
            )

            // Optional content: Show any composable passed into the content lambda
            content?.invoke() // If content is provided, display it here
        }
    }
}


@Composable
fun ElevatedCardCalorieTracker(
    title: String,
    currentCalories: Float = 0.0f,
    goalCalories: Float = 1.0f, // Default value to prevent division by zero
    modifier: Modifier = Modifier,
    gradientStart: Color = MaterialTheme.colorScheme.primary,
    gradientEnd: Color = MaterialTheme.colorScheme.secondary,
    trackColor: Color = White,
    strokeWidth: Dp = 8.dp
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = modifier.size(width = 300.dp, height = 200.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Text content on the left
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "$currentCalories / $goalCalories kcal",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            // Progress indicator on the right
            CalorieProgressIndicator(
                currentCalories = currentCalories,
                goalCalories = goalCalories,
                modifier = Modifier.size(100.dp),
                gradientStart = gradientStart,
                gradientEnd = gradientEnd,
                trackColor = trackColor,
                strokeWidth = strokeWidth
            )
        }
    }
}


    @Composable
    fun ElevatedCardWaterTracker(
        title: String,
        currentIntake: Float = 0.0f,
        goalIntake: Float = 1.0f, // Default value to prevent division by zero
        unit: String,
        modifier: Modifier = Modifier,
        gradientStart: Color = MaterialTheme.colorScheme.primary,
        gradientEnd: Color = MaterialTheme.colorScheme.secondary,
        trackColor: Color = White,
        strokeWidth: Dp = 8.dp
    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = modifier.size(width = 180.dp, height = 200.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Text content on the left
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "$currentIntake / $goalIntake $unit",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    WaterProgressIndicator(
                        currentIntake = currentIntake,
                        goalIntake = goalIntake,
                        modifier = Modifier.size(50.dp).align(Alignment.CenterHorizontally),
                        gradientStart = gradientStart,
                        gradientEnd = gradientEnd,
                        trackColor = trackColor,
                        strokeWidth = strokeWidth
                    )
                }

                // Progress indicator on the right

            }
        }

}


