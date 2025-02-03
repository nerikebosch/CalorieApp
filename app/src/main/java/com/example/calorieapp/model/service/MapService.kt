package com.example.calorieapp.model.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import android.location.Location

/**
 * Service interface for handling map-related operations such as location tracking.
 */
interface MapService {
    /**
     * Represents the current tracking state (active or inactive).
     */
    val trackingState: StateFlow<Boolean>

    /**
     * Flow that emits location updates as they become available.
     */
    val locationUpdates: Flow<Location> // Add this to expose location updates

    /**
     * Initializes location tracking.
     */
    suspend fun initializeTracking()

    /**
     * Checks and requests necessary location permissions.
     * @return `true` if permissions are granted, `false` otherwise.
     */
    suspend fun checkAndRequestPermissions(): Boolean

    /**
     * Starts continuous location tracking.
     */
    suspend fun startContinuousTracking()

    /**
     * Retrieves the last known location of the device.
     * @return The last known [Location], or `null` if unavailable.
     */
    suspend fun getLastKnownLocation(): Location?

    /**
     * Cleans up resources related to location tracking.
     */
    fun cleanup()
}