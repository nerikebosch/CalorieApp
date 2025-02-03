package com.example.calorieapp.screens.activity_stats

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat


/**
 * A composable function that manages and remembers the state of location permissions.
 *
 * This function checks if the required location permissions (ACCESS_FINE_LOCATION and
 * ACCESS_COARSE_LOCATION) are granted. If not, it requests them using the Activity Result API.
 * When the permission is granted, the provided [onPermissionGranted] callback is invoked.
 *
 * @param context The current context to check and request permissions.
 * @param onPermissionGranted A callback function that gets executed when the permission is granted.
 * @return A [State]<Boolean> representing whether the location permission is granted or not.
 */
@Composable
fun rememberLocationPermissionState(
    context: Context,
    onPermissionGranted: () -> Unit
): State<Boolean> {
    var isPermissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        isPermissionGranted = permissions.values.all { it }
        if (isPermissionGranted) {
            onPermissionGranted()
        }
    }

    LaunchedEffect(Unit) {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (!isPermissionGranted) {
            launcher.launch(permissions)
        } else {
            onPermissionGranted()
        }
    }

    return remember { derivedStateOf { isPermissionGranted } }
}