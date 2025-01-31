package com.example.calorieapp.screens.activity_stats

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calorieapp.model.UserActivity
import com.example.calorieapp.model.ActivityStats
import com.example.calorieapp.model.service.LocationService
import com.example.calorieapp.model.service.LogService
import com.example.calorieapp.model.service.StorageService
import com.example.calorieapp.model.service.impl.LocationServiceImpl
import com.example.calorieapp.screens.CalorieAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import java.time.LocalDate


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
        (locationService as? LocationServiceImpl)?.cleanup()
    }
}