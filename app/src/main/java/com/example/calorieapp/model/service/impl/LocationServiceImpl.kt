package com.example.calorieapp.model.service.impl

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.calorieapp.model.UserActivity
import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.LocationService
import com.example.calorieapp.model.service.StepDetector
import com.example.calorieapp.model.service.StorageService
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


class LocationServiceImpl @Inject constructor(
    private val context: Context,
    private val storageService: StorageService,
    private val accountService: AccountService,
) : LocationService{

    private val stepDetector: StepDetector
    private val periodicSaveJob: Job
    private val midnightResetJob: Job


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
    }

    override fun initializeTracking() {
        if (checkAndRequestPermissions()) {
            startContinuousTracking()
        }
    }

    override fun checkAndRequestPermissions(): Boolean {
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

    override fun startContinuousTracking() {
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
        // Similar to updateActivityMetrics, but can be called periodically
        val currentDate = LocalDate.now().format(FIREBASE_DATE_FORMATTER)
        val steps = stepDetector.getCurrentSteps()

        val distanceInMeters = calculateDistance(steps)
        val caloriesBurned = calculateCaloriesBurned(steps)

        val userActivity = UserActivity(
            date = currentDate,
            steps = steps,
            distanceInMeters = distanceInMeters,
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
        // Optional: Implement background location tracking
        // This could use FusedLocationProviderClient for more detailed movement tracking
    }

    private fun calculateDistance(steps: Int): Double = steps * 0.7
    private fun calculateCaloriesBurned(steps: Int): Double = steps * 0.04


    // Cleanup method
    fun cleanup() {
        stepDetector.stop()
        periodicSaveJob.cancel()
        midnightResetJob.cancel()
    }

}
