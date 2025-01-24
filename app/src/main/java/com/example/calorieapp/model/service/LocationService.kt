package com.example.calorieapp.model.service

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationService {
    suspend fun startLocationUpdates()
    suspend fun stopLocationUpdates()
    suspend fun getDistance(): Flow<Double>
    suspend fun getStepCount(): Flow<Int>
    suspend fun calculateCaloriesBurned(): Flow<Double>
    suspend fun getActivityStats(): Flow<ActivityStats>
    suspend fun syncActivityData()
    suspend fun loadTodayActivity()
}

data class ActivityStats(
    val totalSteps: Int,
    val distanceInMeters: Double,
    val caloriesBurned: Double
)

data class RouteInfo(
    val startTime: Long,
    val endTime: Long,
    val points: List<Location>,
    val totalDistance: Double,
    val caloriesBurned: Double
)