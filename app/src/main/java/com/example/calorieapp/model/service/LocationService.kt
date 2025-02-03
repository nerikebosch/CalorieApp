package com.example.calorieapp.model.service

import kotlinx.coroutines.flow.StateFlow

/**
 * Service interface for handling location tracking.
 */
interface LocationService {

    /**
     * Represents the current tracking state.
     */
    val trackingState: StateFlow<Boolean>

    /**
     * Initializes location tracking.
     */
    suspend fun initializeTracking()
    // Check and request permissions

    /**
     * Checks and requests necessary location permissions.
     * @return True if permissions are granted, false otherwise.
     */
    suspend fun checkAndRequestPermissions(): Boolean
    // Continuous tracking method

    /**
     * Starts continuous location tracking.
     */
    suspend fun startContinuousTracking()

    /**
     * Cleans up any resources related to location tracking.
     */
    fun cleanup()
}

