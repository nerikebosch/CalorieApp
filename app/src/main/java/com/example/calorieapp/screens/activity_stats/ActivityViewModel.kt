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

    // State to represent current activity
    private val _activityState = MutableStateFlow<UserActivity>(UserActivity())
    val activityState: StateFlow<UserActivity> = _activityState.asStateFlow()

    // Tracking status
    private val _isTracking = MutableStateFlow(false)
    val isTracking: StateFlow<Boolean> = _isTracking.asStateFlow()

    init {
        // Load today's activity when ViewModel is initialized
        println("ActivityDebug: ActivityViewModel init")
        loadTodayActivity()
        launchCatching {
            storageService.userActivity.collect { activities ->
                val today = LocalDate.now().format(LocationServiceImpl.FIREBASE_DATE_FORMATTER)
                _activityState.value = activities.find { it.date == today } ?: UserActivity(date = today)

            }
        }

    }

    fun startObservingTrackingState() {
        launchCatching {
            locationService.trackingState.collect { isTracking ->
                _isTracking.value = isTracking
            }
        }
    }

    private fun loadTodayActivity() {
        launchCatching {
            val today = LocalDate.now().format(LocationServiceImpl.FIREBASE_DATE_FORMATTER)
            val activity = storageService.getUserActivityByDate(today) ?: UserActivity(date = today)
            println("ActivityDebug: Loaded activity: $activity")
            _activityState.value = activity

            if (activity.id.isEmpty()) {
                println("ActivityDebug: Saving new activity: $activity")
                val newId = storageService.saveUserActivity(activity)
                println("ActivityDebug: new activity ID: $newId")
            } else {
                println("ActivityDebug: Updating EXISTS activity: $activity")
                storageService.updateUserActivity(activity)
            }
        }
    }


    // Periodic update method
    fun refreshActivity() {
        loadTodayActivity()
    }

    override fun onCleared() {
        super.onCleared()
        // Cleanup resources when ViewModel is destroyed
        locationService.cleanup()
    }
}