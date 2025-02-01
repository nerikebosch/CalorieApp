package com.example.calorieapp.screens.activity_stats

import com.example.calorieapp.model.UserActivity
import com.example.calorieapp.model.service.LocationService
import com.example.calorieapp.model.service.LogService
import com.example.calorieapp.model.service.StorageService
import com.example.calorieapp.model.service.impl.LocationServiceImpl
import com.example.calorieapp.screens.CalorieAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val locationService: LocationService,
    private val storageService: StorageService,
    logService: LogService
) : CalorieAppViewModel(logService) {

    private val _activityState = MutableStateFlow<UserActivity>(UserActivity())
    val activityState: StateFlow<UserActivity> = _activityState.asStateFlow()

    private val _isTracking = MutableStateFlow(false)
    val isTracking: StateFlow<Boolean> = _isTracking.asStateFlow()

    private val _permissionGranted = MutableStateFlow(false)
    val permissionGranted: StateFlow<Boolean> = _permissionGranted.asStateFlow()

    init {
        println("ActivityDebug: ActivityViewModel init")
        loadTodayActivity()
        startObservingTrackingState()
        observeUserActivity()
    }

    private fun observeUserActivity() {
        launchCatching {
            storageService.userActivity.collect { activities ->
                val today = LocalDate.now().format(LocationServiceImpl.FIREBASE_DATE_FORMATTER)
                _activityState.value = activities.find { it.date == today } ?: UserActivity(date = today)
            }
        }
    }

    fun onPermissionGranted() {
        _permissionGranted.value = true
        launchCatching {
            locationService.initializeTracking()
        }
    }

    fun startObservingTrackingState() {
        launchCatching {
            locationService.trackingState.collect { isTracking ->
                _isTracking.value = isTracking
                println("ActivityDebug: Tracking state changed to: $isTracking")
            }
        }
    }

    private fun loadTodayActivity() {
        launchCatching {
            val today = LocalDate.now().format(LocationServiceImpl.FIREBASE_DATE_FORMATTER)
            val activity = storageService.getUserActivityByDate(today) ?: UserActivity(date = today)
            _activityState.value = activity
            println("ActivityDebug: Loaded activity: $activity")
        }
    }

    fun refreshActivity() {
        loadTodayActivity()
    }

    override fun onCleared() {
        super.onCleared()
        locationService.cleanup()
    }
}