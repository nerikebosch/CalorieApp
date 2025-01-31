package com.example.calorieapp.model.service

interface LocationService {
    fun initializeTracking()
    // Check and request permissions
    fun checkAndRequestPermissions(): Boolean
    // Continuous tracking method
    fun startContinuousTracking()


}

