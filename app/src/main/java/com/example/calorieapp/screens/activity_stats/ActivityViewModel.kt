package com.example.calorieapp.screens.activity_stats

import androidx.lifecycle.viewModelScope
import com.example.calorieapp.common.snackbar.SnackbarManager
import com.example.calorieapp.common.snackbar.SnackbarMessage.Companion.toSnackbarMessage
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
import java.time.LocalDate
import javax.inject.Inject


/**
 * ViewModel responsible for managing user activity tracking, permissions, and data storage.
 *
 * @property locationService Service responsible for handling location tracking.
 * @property storageService Service for managing user activity storage.
 * @property logService Service for logging application events.
 */
@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val locationService: LocationService,
    private val storageService: StorageService,
    logService: LogService
) : CalorieAppViewModel(logService) {

    private val _activityState = MutableStateFlow(UserActivity())
    /** StateFlow representing the current user activity. */
    val activityState: StateFlow<UserActivity> = _activityState.asStateFlow()

    private val _isTracking = MutableStateFlow(false)
    /** StateFlow representing whether activity tracking is active. */
    val isTracking: StateFlow<Boolean> = _isTracking.asStateFlow()

    private val _permissionGranted = MutableStateFlow(false)
    /** StateFlow indicating if location permission has been granted. */
    val permissionGranted: StateFlow<Boolean> = _permissionGranted.asStateFlow()

    init {
        launchCatching {
            startObservingTrackingState()
            observeUserActivity()
        }
    }

    /**
     * Handles the event when location permission is granted.
     * Initializes tracking if it was not previously granted.
     */
    fun onPermissionGranted() {
        if (!_permissionGranted.value) {
            _permissionGranted.value = true
            launchCatching {
                locationService.initializeTracking()
            }
        }
    }

    /**
     * Starts observing the tracking state changes from the LocationService.
     */
    private fun startObservingTrackingState() {
        launchCatching {
            locationService.trackingState.collect { isTracking ->
                _isTracking.value = isTracking
            }
        }
    }

    /**
     * Observes the user's activity and updates the state with daily activity data.
     * If no data exists for the current day, it creates a new entry.
     */
    private fun observeUserActivity() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                storageService.userActivity
                    .distinctUntilChanged()
                    .collect { activities ->
                        val today = LocalDate.now().format(LocationServiceImpl.FIREBASE_DATE_FORMATTER)
                        val currentActivity = activities.find { it.date == today }
                            ?: UserActivity(date = today).also {
                                // Save new activity if it doesn't exist
                                storageService.saveUserActivity(it)
                            }
                        _activityState.value = currentActivity
                        println("ObserverActDebug: Activity updated - Steps: ${currentActivity.steps}, Distance: ${currentActivity.distanceInMeters}")
                    }
            } catch (e: Exception) {
                println("Error observing activity: ${e.message}")
                _activityState.value = UserActivity(date = LocalDate.now().format(LocationServiceImpl.FIREBASE_DATE_FORMATTER))
            }
        }
    }

    /**
     * Refreshes the user's activity data for the current day.
     * If no activity data exists, it creates and saves a new entry.
     */
    fun refreshActivity() {
        launchCatching {
            val today = LocalDate.now().format(LocationServiceImpl.FIREBASE_DATE_FORMATTER)
            try {
                val currentActivity = storageService.getUserActivityByDate(today)
                    ?: UserActivity(date = today).also {
                        storageService.saveUserActivity(it)
                    }
                _activityState.value = currentActivity
                println("RefreshDebug: Activity refreshed - Steps: ${currentActivity.steps}, Distance: ${currentActivity.distanceInMeters}")
            } catch (e: Exception) {
                println("Error refreshing activity: ${e.message}")
                SnackbarManager.showMessage(e.toSnackbarMessage())
            }
        }
    }

    /**
     * Cleans up resources when the ViewModel is cleared.
     * Ensures that location tracking is properly stopped.
     */
    override fun onCleared() {
        super.onCleared()
        launchCatching {
            locationService.cleanup()
        }
    }
}
