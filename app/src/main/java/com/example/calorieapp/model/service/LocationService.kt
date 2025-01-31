package com.example.calorieapp.model.service

import kotlinx.coroutines.flow.StateFlow

interface LocationService {
    val trackingState: StateFlow<Boolean>
    suspend fun initializeTracking()
    // Check and request permissions
    suspend fun checkAndRequestPermissions(): Boolean
    // Continuous tracking method
    suspend fun startContinuousTracking()

    fun cleanup()
}

