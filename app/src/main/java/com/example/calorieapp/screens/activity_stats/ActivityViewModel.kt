package com.example.calorieapp.screens.activity_stats

import android.Manifest
import androidx.core.app.ActivityCompat
import com.example.calorieapp.model.UserActivity
import com.example.calorieapp.model.service.ActivityStats
import com.example.calorieapp.model.service.LocationService
import com.example.calorieapp.model.service.LogService
import com.example.calorieapp.model.service.StorageService
import com.example.calorieapp.screens.CalorieAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject
import kotlinx.coroutines.delay

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val locationService: LocationService,
    logService: LogService,
    private val storageService: StorageService
) : CalorieAppViewModel(logService) {

    private val _activityStats = MutableStateFlow<ActivityStats?>(null)
    val activityStats: StateFlow<ActivityStats?> get() = _activityStats

    private val _todayActivity = MutableStateFlow<UserActivity?>(null)
    val todayActivity: StateFlow<UserActivity?> = _todayActivity.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    init {
        initializeTracking()
    }

    private fun initializeTracking() {
        launchCatching {
            locationService.loadTodayActivity()
            locationService.startLocationUpdates()
            startPeriodicSync()
            observeActivityStats()
        }
    }

    private fun startPeriodicSync() {
        launchCatching {
            while (true) {
                delay(1 * 1000) // Sync every 10 sec
                locationService.syncActivityData()
            }
        }
    }

    private fun observeActivityStats() {
        launchCatching {
            locationService.getActivityStats()
                .catch { e ->
                    _errorMessage.value = e.message
                }
                .collect { stats ->
                    _activityStats.value = stats
                }
        }
    }

    private fun updateTodayActivity(stats: ActivityStats) {
        launchCatching {
            _todayActivity.value?.let { currentActivity ->
                val updatedActivity = currentActivity.copy(
                    steps = stats.totalSteps,
                    distanceInMeters = stats.distanceInMeters,
                    caloriesBurned = stats.caloriesBurned
                )
                _todayActivity.value = updatedActivity
            }
        }
    }

    fun refreshStats() {
        launchCatching {
            locationService.loadTodayActivity()
            locationService.syncActivityData()
        }
    }

    override fun onCleared() {
        super.onCleared()
        launchCatching {
            locationService.stopLocationUpdates()
        }
    }


}