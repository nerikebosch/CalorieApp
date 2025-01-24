package com.example.calorieapp.model.service.impl

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.calorieapp.model.UserActivity
import com.example.calorieapp.model.service.ActivityStats
import com.example.calorieapp.model.service.LocationService
import com.example.calorieapp.model.service.StorageService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject


class LocationServiceImpl @Inject constructor(
    private val context: Context,
    private val locationClient: FusedLocationProviderClient,
    private val sensorManager: SensorManager,
    private val storageService: StorageService
) : LocationService {

    private val _stepCountFlow = MutableStateFlow(0)
    private val _caloriesFlow = MutableStateFlow(0.0)
    private val _distanceFlow = MutableStateFlow(0.0)


    private var lastLocation: Location? = null
    private var initialSteps = 0
    private var stepCountOffset = 0

    private val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L).apply{
        setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
        setMinUpdateIntervalMillis(1000L)
        setWaitForAccurateLocation(true)
    }.build()


    private val stepSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    init {
        if (stepSensor == null) {
            Log.e("LocationServiceDebug", "Step counter sensor is not available on this device")
        }
        loadPreviousDayActivity()
        registerStepSensor()
    }

    private fun loadPreviousDayActivity() {
        runBlocking {
            val todayDateString = LocalDate.now().format(FIREBASE_DATE_FORMATTER)
            val todayActivity = storageService.getUserActivityByDate(todayDateString)
                ?: UserActivity(date = todayDateString)

            _stepCountFlow.value = todayActivity.steps
            _caloriesFlow.value = todayActivity.caloriesBurned
            _distanceFlow.value = todayActivity.distanceInMeters
            stepCountOffset = todayActivity.steps
        }
    }

    override suspend fun startLocationUpdates() {
        if (checkLocationPermission()) {
            startLocationTracking()
        }
    }

    override suspend fun stopLocationUpdates() {
        locationClient.removeLocationUpdates(locationCallback)
        sensorManager.unregisterListener(stepSensorListener)
    }

    override suspend fun getDistance(): Flow<Double> = _distanceFlow.asStateFlow()
    override suspend fun getStepCount(): Flow<Int> = _stepCountFlow.asStateFlow()
    override suspend fun calculateCaloriesBurned(): Flow<Double> = _caloriesFlow.asStateFlow()

    override suspend fun getActivityStats(): Flow<ActivityStats> = combine(
        getStepCount(),
        getDistance(),
        calculateCaloriesBurned(),
    ) { steps, distance, calories ->
        ActivityStats(
            totalSteps = steps,
            distanceInMeters = distance,
            caloriesBurned = calories,
        )
    }

    override suspend fun syncActivityData() {
        try {
            val todayDateString = LocalDate.now().format(FIREBASE_DATE_FORMATTER)
            val todayActivity = storageService.getUserActivityByDate(todayDateString) ?: UserActivity(date = todayDateString)

            val updatedActivity = todayActivity.copy(
                steps = _stepCountFlow.value,
                distanceInMeters = _distanceFlow.value,
                caloriesBurned = _caloriesFlow.value,
                lastSyncTimestamp = System.currentTimeMillis()
            )

            if (updatedActivity.id.isEmpty()) {
                storageService.saveUserActivity(updatedActivity)
            } else {
                storageService.updateUserActivity(updatedActivity)
            }
        } catch (e: Exception) {
            Log.e("LocationService", "Error syncing activity data", e)
            throw e
        }
    }


    override suspend fun loadTodayActivity() {
        try {
            val todayDateString = LocalDate.now().format(FIREBASE_DATE_FORMATTER)
            val todayActivity = storageService.getUserActivityByDate(todayDateString) ?: UserActivity(date = todayDateString, lastSyncTimestamp = System.currentTimeMillis())

            _stepCountFlow.value = todayActivity.steps
            _caloriesFlow.value = todayActivity.caloriesBurned
            _distanceFlow.value = todayActivity.distanceInMeters
        } catch (e: Exception) {
            Log.e("Debug: LocationService", "Error loading today's activity", e)
            throw e
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.lastLocation?.let { newLocation ->
                Log.d("LocationService", "Location update received: $newLocation") // Add this log
                updateLocationData(newLocation)
            }
        }
    }

    private val stepSensorListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            event.values.firstOrNull()?.let { steps ->
                if (initialSteps == 0)  initialSteps = steps.toInt()

                Log.d("StepSensor", "Step event received: $steps")
                val previousSteps = _stepCountFlow.value
                val currentSteps = steps.toInt() - initialSteps + stepCountOffset

                if (currentSteps > previousSteps) {
                    _stepCountFlow.value = currentSteps
                    updateCaloriesBurned(currentSteps)
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    private fun calculateDistance(last: Location, current: Location): Double {
        return last.distanceTo(current).toDouble() // Returns distance in meters
    }

    private fun updateLocationData(newLocation: Location) {
        lastLocation?.let { last ->
            val distance = calculateDistance(last, newLocation)
            _distanceFlow.value += distance
            updateCaloriesBurned(_stepCountFlow.value)
        }
        lastLocation = newLocation
    }

    private fun updateCaloriesBurned(steps: Int) {
        val caloriesFromSteps = steps * CALORIES_PER_STEP
        val caloriesFromDistance = _distanceFlow.value * CALORIES_PER_METER
        _caloriesFlow.value = caloriesFromSteps + caloriesFromDistance
    }

    private fun registerStepSensor() {
        stepSensor?.let {
            sensorManager.registerListener(
                stepSensorListener,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )

        }
    }

    private fun startLocationTracking() {
        if (checkLocationPermission()) {
            try {
                locationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            } catch (e: SecurityException) {
                // Handle permission error
                Log.e("LocationService", "Permission error", e)
                throw e
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val CALORIES_PER_STEP = 0.04
        private const val CALORIES_PER_METER = 0.06
        val FIREBASE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
        private val APP_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    }
}