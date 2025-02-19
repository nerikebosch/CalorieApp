package com.example.calorieapp.screens.activity_stats

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calorieapp.common.composable.BasicToolbar
import com.example.calorieapp.model.UserActivity
import com.example.calorieapp.R.string as AppText

/**
 * Composable function that displays the Activity Screen, which includes activity tracking status,
 * location permission status, and activity statistics.
 *
 * @param popUpScreen Callback function to handle screen navigation.
 * @param viewModel ViewModel that provides activity tracking data and state management.
 */
@Composable
fun ActivityScreen(
    popUpScreen: () -> Unit,
    viewModel: ActivityViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activityState by viewModel.activityState.collectAsState()
    val isTracking by viewModel.isTracking.collectAsState()
    val permissionGranted by viewModel.permissionGranted.collectAsState()

    val locationPermissionState = rememberLocationPermissionState(
        context = context,
        onPermissionGranted = { viewModel.onPermissionGranted() }
    )

    LaunchedEffect(locationPermissionState.value) {
        if (locationPermissionState.value) {
            viewModel.onPermissionGranted()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        BasicToolbar(AppText.activity_tracker)

        // Permission Status
        PermissionStatusCard(locationPermissionState.value)

        // Tracking Status
        TrackingStatusCard(isTracking)

        // Activity Stats
        ActivityStats(activityState, viewModel, locationPermissionState.value)
    }
}

/**
 * Composable function that displays the current status of location permission.
 *
 * @param isGranted Boolean indicating whether location permission is granted.
 */
@Composable
fun PermissionStatusCard(isGranted: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isGranted) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Text(
            text = "Location Permission: ${if (isGranted) "Granted" else "Not Granted"}",
            modifier = Modifier.padding(16.dp),
            color = if (isGranted) MaterialTheme.colorScheme.onPrimaryContainer
            else MaterialTheme.colorScheme.onErrorContainer
        )
    }
}

/**
 * Composable function that displays the tracking status of the activity.
 *
 * @param isTracking Boolean indicating whether activity tracking is currently active.
 */
@Composable
fun TrackingStatusCard(isTracking: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isTracking) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Text(
            text = "Tracking Status: ${if (isTracking) "Active" else "Inactive"}",
            modifier = Modifier.padding(16.dp),
            color = if (isTracking) MaterialTheme.colorScheme.onPrimaryContainer
            else MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

/**
 * Composable function that displays user activity statistics such as steps taken, distance covered,
 * and calories burned.
 *
 * @param activityState Current state of the user's activity data.
 * @param viewModel ViewModel responsible for managing activity data.
 * @param isPermissionGranted Boolean indicating whether location permission is granted.
 */
@Composable
fun ActivityStats(activityState: UserActivity, viewModel: ActivityViewModel, isPermissionGranted: Boolean) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Steps: ${activityState.steps}", style = MaterialTheme.typography.headlineMedium)
            Text("Distance: ${String.format("%.2f", activityState.distanceInMeters)} m", style = MaterialTheme.typography.bodyLarge)
            Text("Calories: ${String.format("%.2f", activityState.caloriesBurned)} kcal", style = MaterialTheme.typography.bodyLarge)

            Button(
                onClick = { viewModel.refreshActivity() },
                enabled = isPermissionGranted
            ) {
                Text("Refresh Activity")
            }
        }
    }
}
