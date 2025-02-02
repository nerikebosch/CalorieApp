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

    private val _activityState = MutableStateFlow(UserActivity())
    val activityState: StateFlow<UserActivity> = _activityState.asStateFlow()

    private val _isTracking = MutableStateFlow(false)
    val isTracking: StateFlow<Boolean> = _isTracking.asStateFlow()

    private val _permissionGranted = MutableStateFlow(false)
    val permissionGranted: StateFlow<Boolean> = _permissionGranted.asStateFlow()

    init {
        startObservingTrackingState()
        loadTodayActivity()
    }

    fun onPermissionGranted() {
        if (!_permissionGranted.value) {
            _permissionGranted.value = true
            viewModelScope.launch {
                locationService.initializeTracking()
            }
        }
    }

    private fun startObservingTrackingState() {
        viewModelScope.launch {
            locationService.trackingState.collect { isTracking ->
                _isTracking.value = isTracking
            }
        }
    }

    private fun loadTodayActivity() {
        viewModelScope.launch(Dispatchers.IO) {
            val today = LocalDate.now().format(LocationServiceImpl.FIREBASE_DATE_FORMATTER)
            val activity = storageService.getUserActivityByDate(today) ?: UserActivity(date = today)
            _activityState.value = activity
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
