package com.example.calorieapp.model.service.impl

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.calorieapp.model.UserActivity
import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.LocationService
import com.example.calorieapp.model.service.StepDetector
import com.example.calorieapp.model.service.StorageService
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import androidx.compose.runtime.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class LocationServiceImpl @Inject constructor(
    private val context: Context,
    private val storageService: StorageService,
    private val accountService: AccountService,
) : LocationService{

    private val stepDetector: StepDetector
    private val periodicSaveJob: Job
    private val midnightResetJob: Job

    // Location tracking improvements
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null
    private var previousLocation: Location? = null
    private var totalDistanceMeters: Double = 0.0

    // Add these new properties for distance tracking
    private var lastUpdateTime: Long = 0
    private val MIN_UPDATE_INTERVAL = 30_000L // 30 seconds
    private val MIN_DISTANCE_CHANGE = 10.0 // 10 meters


    private val _trackingState = MutableStateFlow(false)
    override val trackingState: StateFlow<Boolean> = _trackingState.asStateFlow()

    companion object {
        val FIREBASE_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        private const val PERMISSION_REQUEST_CODE = 1001
        // Permission request constants
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.ACTIVITY_RECOGNITION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }


    init {
        // Initialize step detector
        stepDetector = StepDetector(context) { steps ->
            updateActivityStats(steps)
        }

        // Periodic saving job
        periodicSaveJob = CoroutineScope(Dispatchers.Default).launch {
            while(true) {
                delay(1 * 60 * 1000) // Save every 1 minutes
                saveCurrentActivity()
            }
        }

        // Midnight reset job
        midnightResetJob = CoroutineScope(Dispatchers.Default).launch {
            while(true) {
                // Calculate time until next midnight
                val now = LocalDateTime.now()
                val nextMidnight = now.toLocalDate().plusDays(1).atStartOfDay()
                val duration = Duration.between(now, nextMidnight)

                // Wait until midnight
                delay(duration.toMillis())

                // Reset daily activity
                resetDailyActivity()
            }
        }

        // Initialize location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    override suspend fun initializeTracking() {
        if (checkAndRequestPermissions()) {
            startContinuousTracking()
        }
    }

    override suspend fun checkAndRequestPermissions(): Boolean {
        // Check if all required permissions are granted
        val permissionsNotGranted = REQUIRED_PERMISSIONS.filter { permission ->
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        }

        // If any permissions are not granted, request them
        if (permissionsNotGranted.isNotEmpty()) {
            // This should typically be called from an activity or fragment
            ActivityCompat.requestPermissions(
                context as Activity,
                permissionsNotGranted.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
            return false
        }

        return true
    }

    override suspend fun startContinuousTracking() {
        // Start step detection
        stepDetector.start()

        // Start location tracking (optional)
        startLocationTracking()
    }



    private fun updateActivityStats(steps: Int) {
        // Calculate metrics based on steps
        val distanceInMeters = calculateDistance(steps)
        val caloriesBurned = calculateCaloriesBurned(steps)

        // Create or update today's activity
        val currentDate = LocalDate.now().format(FIREBASE_DATE_FORMATTER)
        val userActivity = UserActivity(
            date = currentDate,
            steps = steps,
            distanceInMeters = distanceInMeters,
            caloriesBurned = caloriesBurned
        )

        // Update in storage
        CoroutineScope(Dispatchers.IO).launch {
            val existingActivity = storageService.getUserActivityByDate(currentDate)

            if (existingActivity != null) {
                storageService.updateUserActivity(
                    userActivity.copy(id = existingActivity.id)
                )
            } else {
                storageService.saveUserActivity(userActivity)
            }
        }
    }

    private fun saveCurrentActivity() {

        val currentDate = LocalDate.now().format(FIREBASE_DATE_FORMATTER)
        val steps = stepDetector.getCurrentSteps()
        val caloriesBurned = calculateCaloriesBurned(steps)

        //val distanceInMeters = calculateDistance(steps)


        val userActivity = UserActivity(
            date = currentDate,
            steps = steps,
            //distanceInMeters = distanceInMeters,
            distanceInMeters = totalDistanceMeters,
            caloriesBurned = caloriesBurned
        )

        CoroutineScope(Dispatchers.IO).launch {
            val existingActivity = storageService.getUserActivityByDate(currentDate)

            if (existingActivity != null) {
                storageService.updateUserActivity(
                    userActivity.copy(id = existingActivity.id)
                )
            } else {
                storageService.saveUserActivity(userActivity)
            }
        }
    }

    private fun resetDailyActivity() {
        // Reset step detector for new day
        stepDetector.resetSteps()
    }

    private fun startLocationTracking() {
        _trackingState.value = true
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 30_000L)
                .setWaitForAccurateLocation(true)
                .setMinUpdateIntervalMillis(10_000L)
                .setMinUpdateDistanceMeters(MIN_DISTANCE_CHANGE.toFloat())
                .build()

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastUpdateTime > MIN_UPDATE_INTERVAL) {
                        locationResult.lastLocation?.let { location ->
                            updateLocationData(location)
                            lastUpdateTime = currentTime
                        }
                    }
                }
            }

            fusedLocationClient?.requestLocationUpdates(
                locationRequest,
                locationCallback!!,
                Looper.getMainLooper()
            )

    }

    // Optimized location data update
    private fun updateLocationData(location: Location) {
        previousLocation?.let { prev ->
            val distance = prev.distanceTo(location).toDouble()
            if (distance > MIN_DISTANCE_CHANGE) {
                totalDistanceMeters += distance
            }
        }
        previousLocation = location
    }

    private fun calculateDistance(steps: Int): Double = steps * 0.7
    private fun calculateCaloriesBurned(steps: Int): Double = steps * 0.04


    // Cleanup method
    // Modified cleanup to stop location updates
    override fun cleanup() {
        _trackingState.value = false
        stepDetector.stop()
        periodicSaveJob.cancel()
        midnightResetJob.cancel()
        locationCallback?.let {
            fusedLocationClient?.removeLocationUpdates(it)
            locationCallback = null
        }

        fusedLocationClient = null

        System.gc()
    }


}
