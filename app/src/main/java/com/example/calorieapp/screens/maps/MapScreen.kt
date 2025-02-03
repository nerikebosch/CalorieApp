package com.example.calorieapp.screens.maps

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition



/**
 * Composable function that renders the Google Map and manages location permissions.
 *
 * @param viewModel ViewModel responsible for managing location data.
 * @param openScreen Function to navigate to another screen.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel(),
    openScreen: (String) -> Unit = {},
) {
    val locationPermissionState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    LaunchedEffect(locationPermissionState) {
        if (!locationPermissionState.allPermissionsGranted) {
            locationPermissionState.launchMultiplePermissionRequest()
        }
    }

    if (!locationPermissionState.allPermissionsGranted) {
        Text("Location permission is required to display the map.")
        return // Exit the composable if permission is not granted
    }

    // Your existing MapScreen content
    val currentLocation by viewModel.currentLocation.collectAsState()
    val locationList by viewModel.locationList.collectAsState()
    val cameraPosition by viewModel.cameraPosition.collectAsState()

    // Default to a valid LatLng (0,0) to avoid null issues
    val defaultLocation = LatLng(37.7749, -122.4194) // San Francisco
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLocation ?: defaultLocation, 15f)
    }

    LaunchedEffect(cameraPosition) {
        cameraPosition?.let {
            println("Updating Camera to: $it")
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(it, 15f))
        }
    }

    LaunchedEffect(currentLocation) {
        currentLocation?.let {
            println("Current Location: $it")
            cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(it, 15f))
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.weight(1f),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = true,
                mapType = MapType.NORMAL
            ),
            uiSettings = MapUiSettings(
                myLocationButtonEnabled = true,
                zoomControlsEnabled = true
            )
        ) {
            // Only add the marker if `currentLocation` is not null
            currentLocation?.let { location ->
                Marker(
                    state = rememberMarkerState(position = location),
                    title = "Current Location"
                )
            }
        }

        Surface(modifier = Modifier.fillMaxWidth().padding(16.dp), shadowElevation = 4.dp) {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                item {
                    Text("Current Location:", style = MaterialTheme.typography.titleMedium)
                }
                currentLocation?.let { location ->
                    item {
                        Text("Lat: ${location.latitude}, Lng: ${location.longitude}")
                    }
                }
            }
        }
    }
}

/**
 * Preview function for [MapScreen].
 */
@Preview
@Composable
fun PreviewMapScreen() {

}
