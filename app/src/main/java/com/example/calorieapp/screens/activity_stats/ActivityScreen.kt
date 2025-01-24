package com.example.calorieapp.screens.activity_stats

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calorieapp.model.service.ActivityStats
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreen(
    popUpScreen: () -> Unit,
    viewModel: ActivityViewModel = hiltViewModel()
) {
    val activityStats by viewModel.activityStats.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Activity Tracker") })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "Unknown Error",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            } else if (activityStats != null) {
                ActivityStatsView(activityStats!!)
            } else {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun ActivityStatsView(stats: ActivityStats) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Steps: ${stats.totalSteps}",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Distance: ${String.format(Locale.getDefault(), "%.2f", stats.distanceInMeters)} m",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Calories Burned: ${String.format(Locale.getDefault(),"%.2f", stats.caloriesBurned)} kcal",
            style = MaterialTheme.typography.bodyLarge
        )

    }
}
