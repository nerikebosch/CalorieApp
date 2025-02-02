package com.example.calorieapp.screens.activity_stats

import androidx.lifecycle.viewModelScope
import com.example.calorieapp.model.UserActivity
import com.example.calorieapp.model.service.LocationService
import com.example.calorieapp.model.service.LogService
import com.example.calorieapp.model.service.StorageService
import com.example.calorieapp.model.service.impl.LocationServiceImpl
import com.example.calorieapp.screens.CalorieAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
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
        startObservingTrackingState()
        observeUserActivity()
    }

    private fun observeUserActivity() {
        launchCatching {
            storageService.userActivity.distinctUntilChanged()
                .collect { activities ->
                    val today = LocalDate.now().format(LocationServiceImpl.FIREBASE_DATE_FORMATTER)
                    val currentActivity = activities.find { it.date == today } ?: UserActivity(date = today).also {storageService.saveUserActivity(it)}
                   _activityState.value = currentActivity

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
        viewModelScope.launch(Dispatchers.IO) {
            val today = LocalDate.now().format(LocationServiceImpl.FIREBASE_DATE_FORMATTER)
            val activity = storageService.getUserActivityByDate(today) ?: UserActivity(date = today)
            _activityState.value = activity
            println("ActivityDebug: Loaded activity: $activity")
        }
    }

    fun refreshActivity() {
        observeUserActivity()
    }

    override fun onCleared() {
        super.onCleared()
        locationService.cleanup()
    }
}