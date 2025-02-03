package com.example.calorieapp.screens.maps

import android.location.Location
import androidx.lifecycle.viewModelScope
import com.example.calorieapp.model.service.*
import com.example.calorieapp.screens.CalorieAppViewModel
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * ViewModel for handling map-related operations, including location tracking, distance calculation,
 * and updating the camera position on the map.
 *
 * @param logService Service for logging events and errors.
 * @param storageService Service for handling data storage operations.
 * @param mapService Service for managing location tracking.
 * @param accountService Service for handling user account information.
 */
@HiltViewModel
class MapViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val mapService: MapService,
    private val accountService: AccountService,
) : CalorieAppViewModel(logService) {

    /** Current user location as [LatLng]. */
    private val _currentLocation = MutableStateFlow<LatLng?>(null)
    val currentLocation = _currentLocation.asStateFlow()

    /** List containing the most recent location point. */
    private val _locationList = MutableStateFlow<List<LatLng>>(emptyList())
    val locationList = _locationList.asStateFlow()

    /** Camera position for the map, set to the latest location. */
    private val _cameraPosition = MutableStateFlow<LatLng?>(null)
    val cameraPosition = _cameraPosition.asStateFlow()

    /** Stores the last known location to calculate distance changes. */
    private var lastLocation: Location? = null

    init {
        launchCatching {
            if (mapService.checkAndRequestPermissions()) {
                mapService.initializeTracking()
                mapService.trackingState.collectLatest { isTracking ->
                    if (isTracking) {
                        startTracking()
                    }
                }
            }
        }

        // Get last known location to immediately update UI
        viewModelScope.launch {
            val lastKnownLocation = mapService.getLastKnownLocation()
            lastKnownLocation?.let { updateLocation(it) }
        }

        // Collect location updates from the LocationService
        viewModelScope.launch {
            mapService.locationUpdates.collect { location ->
                updateLocation(location)
            }
        }
    }

    /**
     * Starts continuous location tracking by requesting location updates.
     */
    fun startTracking() {
        viewModelScope.launch {
            mapService.startContinuousTracking()
        }
    }

    /**
     * Stops location tracking and cleans up resources.
     */
    fun stopTracking() {
        mapService.cleanup()
    }

    /**
     * Updates the current location, camera position, and calculates distance traveled.
     *
     * @param location The new location received from location updates.
     */
    fun updateLocation(location: Location) {
        val newLatLng = LatLng(location.latitude, location.longitude)
        _currentLocation.value = newLatLng
        _cameraPosition.value = newLatLng // Move camera to new location

        // Keep only the latest location in the list
        _locationList.value = listOf(newLatLng)


        // Calculate distance if we have previous locations
        lastLocation?.let { last ->
            val distance = last.distanceTo(location)
        }
        lastLocation = location
    }
}