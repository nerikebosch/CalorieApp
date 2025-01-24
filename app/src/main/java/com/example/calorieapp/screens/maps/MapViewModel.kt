//package com.example.calorieapp.screens.maps
//
//import android.location.Location
//import com.example.calorieapp.model.service.AccountService
//import com.example.calorieapp.model.service.LocationService
//import com.example.calorieapp.model.service.LogService
//import com.example.calorieapp.model.service.StorageService
//import com.example.calorieapp.screens.CalorieAppViewModel
//import com.google.android.gms.maps.model.LatLng
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import javax.inject.Inject
//
//@HiltViewModel
//class MapViewModel @Inject constructor(
//    logService: LogService,
//    private val storageService: StorageService,
//    private val locationService: LocationService,
//    private val accountService: AccountService,
//) : CalorieAppViewModel(logService) {
//
//    private val _currentLocation = MutableStateFlow<LatLng?>(null)
//    val currentLocation = _currentLocation.asStateFlow()
//
//    private val _totalDistance = MutableStateFlow(0f)
//    val totalDistance = _totalDistance.asStateFlow()
//
//    private val _locationList = MutableStateFlow<List<LatLng>>(emptyList())
//    val locationList = _locationList.asStateFlow()
//
//    private val _caloriesBurned = MutableStateFlow(0f)
//    val caloriesBurned = _caloriesBurned.asStateFlow()
//
//    private var lastLocation: Location? = null
//
//    fun startTracking() {
//        locationService.startLocationUpdates { location ->
//            updateLocation(location)
//        }
//    }
//
//    fun stopTracking() {
//        locationService.stopLocationUpdates()
//    }
//
//    fun calculateCaloriesBurned() {
//        val distanceInMeters = _totalDistance.value
//    }
//
//    private fun updateDistance(newLocation: Location) {
//        lastLocation?.let { last ->
//            val distance = last.distanceTo(newLocation)
//            _totalDistance.value += distance
//            _caloriesBurned.value = locationService.calculateCaloriesBurned(_totalDistance.value)
//        }
//        lastLocation = newLocation
//    }
//
//    fun updateLocation(location: Location) {
//        val newLatLng = LatLng(location.latitude, location.longitude)
//        _currentLocation.value = newLatLng
//
//        // Add to location list
//        val currentList = _locationList.value.toMutableList()
//        currentList.add(newLatLng)
//        _locationList.value = currentList
//
//        // Calculate distance if we have previous locations
//        if (currentList.size > 1) {
//            val lastTwo = currentList.takeLast(2)
//            val results = FloatArray(1)
//            Location.distanceBetween(
//                lastTwo[0].latitude, lastTwo[0].longitude,
//                lastTwo[1].latitude, lastTwo[1].longitude,
//                results
//            )
//            _totalDistance.value += results[0]
//        }
//    }
//}