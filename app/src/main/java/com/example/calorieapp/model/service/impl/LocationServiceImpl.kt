package com.example.calorieapp.model.service.impl

import android.Manifest
import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.BatteryManager
import android.os.Build
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.example.calorieapp.model.User
import com.example.calorieapp.model.UserActivity
import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.LocationService
import com.example.calorieapp.model.service.StorageService
import com.google.android.gms.location.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject


class LocationServiceImpl @Inject constructor(
    private val context: Context,
    private val storageService: StorageService,
    private val accountService: AccountService,
) : LocationService {

    // Default user metrics as constants
    private object DefaultMetrics {
        const val HEIGHT = 1.7f  // meters
        const val WEIGHT = 70f   // kg
        const val STRIDE_LENGTH = 0.7 // meters
    }

    data class ActivityStats(
        val distance: Double = 0.0,
        val steps: Int = 0,
        val calories: Double = 0.0,
        val speed: Double = 0.0,
        val isMoving: Boolean = false
    )
    // User state
    private val serviceScope = CoroutineScope(Dispatchers.Default)
    private val user = accountService.currentUser
        .stateIn(
            scope = serviceScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = User()
        )

    private fun getWeight(): Double {
        return (user.value.weight.takeIf { it > 0 } ?: DefaultMetrics.WEIGHT) as Double
    }

    private fun getHeight(): Double {
        return (user.value.height.takeIf { it > 0 } ?: DefaultMetrics.HEIGHT) as Double
    }

    private fun getStrideLength(): Double {
        return getHeight() * 0.413
    }
    private val _activityStats = MutableStateFlow(ActivityStats())
    val activityStats: StateFlow<ActivityStats> = _activityStats.asStateFlow()

    private val _trackingState = MutableStateFlow(false)
    override val trackingState: StateFlow<Boolean> = _trackingState.asStateFlow()

    // Location tracking
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null
    private var previousLocation: Location? = null
    private var locationUpdateJob: Job? = null

    // Add persistent storage
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("activity_tracker", Context.MODE_PRIVATE)
    }

    private fun saveState() {
        with(prefs.edit()) {
            putLong("distance", totalDistanceMeters.toBits())
            putInt("steps", totalSteps)
            putLong("calories", totalCalories.toBits())
            putLong("last_update", lastUpdateTime)
            apply()
        }

    }

    private fun loadState() {
        totalDistanceMeters = prefs.getLong("distance", 0.0.toBits()).let { Double.fromBits(it) }
        totalSteps = prefs.getInt("steps", 0)
        totalCalories = prefs.getLong("calories", 0.0.toBits()).let { Double.fromBits(it) }
        lastUpdateTime = prefs.getLong("last_update", 0)
    }

    // Activity metrics
    private var totalDistanceMeters: Double = 0.0
    private var totalSteps: Int = 0
    private var totalCalories: Double = 0.0
    private var lastUpdateTime: Long = 0

    // Configuration constants
    companion object {
        private const val MIN_UPDATE_INTERVAL = 10_000L // 10 seconds
        private const val MIN_DISTANCE_CHANGE = 5.0 // 5 meters
        private const val MAX_ACCURACY_THRESHOLD = 10f // meters
        private const val MAX_WALKING_SPEED = 10f // meters/second
        private const val BATTERY_SAVER_THRESHOLD = 15
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001

        val FIREBASE_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    // Background jobs
    private val periodicSaveJob: Job
    private val midnightResetJob: Job

    init {
        // Initialize location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        // Initialize periodic saving
        periodicSaveJob = CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                delay(10_000) // Save every 10 sec
                saveCurrentActivity()
            }
        }

        // Initialize midnight reset
        midnightResetJob = CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                val now = LocalDateTime.now()
                val nextMidnight = now.toLocalDate().plusDays(1).atStartOfDay()
                val duration = Duration.between(now, nextMidnight)
                delay(duration.toMillis())
                resetDailyActivity()
            }
        }
    }

    override suspend fun initializeTracking() {
        loadState()
        if (checkAndRequestPermissions()) {
            startLocationTracking()
        }
    }

    override suspend fun checkAndRequestPermissions(): Boolean {
        val permissionsNotGranted = REQUIRED_PERMISSIONS.filter { permission ->
            ContextCompat.checkSelfPermission(context, permission) !=
                    PackageManager.PERMISSION_GRANTED
        }

        if (permissionsNotGranted.isNotEmpty()) {
            if (context is Activity) {
                ActivityCompat.requestPermissions(
                    context,
                    permissionsNotGranted.toTypedArray(),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
            return false
        }
        return true
    }

    override suspend fun startContinuousTracking() {
        if (!checkAndRequestPermissions()) return
        startLocationTracking()
    }


    private fun startLocationTracking() {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        _trackingState.value = true

        // Configure location request based on battery level
        val batteryManager = context.getSystemService<BatteryManager>()
        val batteryLevel = batteryManager?.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY) ?: 100


        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5_000L
        )
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(3_000L)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    if (isAccurateLocation(location)) {
                        processLocationUpdate(location)
                    }
                }
            }
        }

        try {
            fusedLocationClient?.requestLocationUpdates(
                locationRequest,
                locationCallback!!,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
            _trackingState.value = false
            // Handle security exception - possibly log or notify user
        } catch (e: Exception) {
            _trackingState.value = false
            // Handle other exceptions
        }
    }

    private fun isAccurateLocation(location: Location): Boolean {
        return location.accuracy <= MAX_ACCURACY_THRESHOLD &&
                location.hasSpeed() &&
                location.speed <= MAX_WALKING_SPEED &&
                validateLocationChange(location)
    }

    private fun validateLocationChange(newLocation: Location): Boolean {
        previousLocation?.let { prevLocation ->
            val timeInterval = (newLocation.time - prevLocation.time) / 1000.0
            if (timeInterval <= 0) return false

            val distance = prevLocation.distanceTo(newLocation).toDouble()
            val speed = distance / timeInterval

            // Check for unrealistic movements
            return speed <= MAX_WALKING_SPEED &&
                    distance >= MIN_DISTANCE_CHANGE &&
                    distance <= (MAX_WALKING_SPEED * timeInterval)
        }
        return true
    }

    private fun processLocationUpdate(location: Location) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastUpdateTime < MIN_UPDATE_INTERVAL) return

        previousLocation?.let { prevLocation ->
            val distance = prevLocation.distanceTo(location).toDouble()
            val timeInterval = (location.time - prevLocation.time) / 1000.0
            val speed = distance / timeInterval

            if (distance >= MIN_DISTANCE_CHANGE) {
                updateActivityMetrics(distance, speed)
                lastUpdateTime = currentTime
                saveState()
            }
        }
        previousLocation = location
    }

    private fun updateActivityMetrics(distance: Double, speed: Double) {
        totalDistanceMeters += distance

        // Calculate calories based on speed and default weight
        val caloriesForSegment = calculateCaloriesForSegment(distance, speed)
        totalCalories += caloriesForSegment

        // Update steps based on distance and default stride length
        val stepsForSegment = calculateSteps(distance)
        totalSteps += stepsForSegment

        // Update activity stats
        _activityStats.value = ActivityStats(
            distance = totalDistanceMeters,
            steps = totalSteps,
            calories = totalCalories,
            speed = speed,
            isMoving = speed > 0.3 // Consider moving if speed > 0.5 m/s
        )

        saveState()
    }

    private fun calculateCaloriesForSegment(
        distance: Double,
        speed: Double,
    ): Double {
        val met = when {
            speed < 1.0 -> 2.0  // slow walking
            speed < 1.5 -> 2.8  // normal walking
            else -> 3.5  // brisk walking
        }

        // Calories = MET * weight (kg) * time (hours)
        // time = distance / speed
        val hours = distance / (speed * 3600) // Convert to hours
        return met * getWeight() * hours * 1.036 // 1.036 converts to calories
    }

    private fun calculateSteps(distance: Double): Int {
        return (distance / getStrideLength()).toInt()
    }

    private fun saveCurrentActivity() {
        val currentDate = LocalDate.now().format(FIREBASE_DATE_FORMATTER)
        val userActivity = UserActivity(
            date = currentDate,
            steps = totalSteps,
            distanceInMeters = totalDistanceMeters,
            caloriesBurned = totalCalories
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                storageService.getUserActivityByDate(currentDate)?.let { existing ->
                    storageService.updateUserActivity(userActivity.copy(id = existing.id))
                } ?: storageService.saveUserActivity(userActivity)
            } catch (e: Exception) {
                // Handle storage errors
            }
        }
    }

    private fun resetDailyActivity() {
        totalDistanceMeters = 0.0
        totalSteps = 0
        totalCalories = 0.0
        _activityStats.value = ActivityStats()
        saveCurrentActivity()
    }

    override fun cleanup() {
        saveState()
        serviceScope.cancel()
        _trackingState.value = false

        runBlocking{
            saveCurrentActivity()
        }

        periodicSaveJob.cancel()
        midnightResetJob.cancel()


        locationCallback?.let {
            fusedLocationClient?.removeLocationUpdates(it)
            locationCallback = null
        }

        fusedLocationClient = null
        previousLocation = null
        lastUpdateTime = 0

        // Cancel any pending coroutines
        locationUpdateJob?.cancel()

        System.gc()
    }


    //Background Tracking
    private val notificationId = 1337
    private fun startForegroundService() {
        val notification = NotificationCompat.Builder(context, "location_channel")
            .setContentTitle("Activity Tracking Active")
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(Intent(context, LocationService::class.java))
        } else {
            context.startService(Intent(context, LocationService::class.java))
        }

        (context as? Service)?.startForeground(notificationId, notification)
    }
}