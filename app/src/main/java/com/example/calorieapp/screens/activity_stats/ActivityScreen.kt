package com.example.calorieapp.screens.activity_stats

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calorieapp.common.composable.BasicToolbar
import com.example.calorieapp.R.string as AppText

@Composable
fun ActivityScreen(
    popUpScreen: () -> Unit,
    viewModel: ActivityViewModel = hiltViewModel()
) {
    val activityState by viewModel.activityState.collectAsState()
    BasicToolbar(AppText.activity_tracker)
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Steps: ${activityState.steps}",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                "Distance: ${String.format("%.2f", activityState.distanceInMeters)} m",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                "Calories: ${String.format("%.2f", activityState.caloriesBurned)} kcal",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }

}

