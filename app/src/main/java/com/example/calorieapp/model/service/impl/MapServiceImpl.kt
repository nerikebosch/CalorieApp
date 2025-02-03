package com.example.calorieapp.model.service.impl

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.example.calorieapp.model.service.MapService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


/**
 * Implementation of [MapService] that provides location tracking using both GPS and fused location provider.
 *
 * @param context The application context.
 */
class MapServiceImpl @Inject constructor(
    private val context: Context,
    //private val locationManager: LocationManager
) : MapService {

    /** Location Manager for GPS tracking. */
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    /** Fused Location Provider Client for retrieving the last known location. */
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    /** SharedFlow to emit location updates. */
    private val _locationUpdates = MutableSharedFlow<Location>(replay = 1)
    override val locationUpdates: SharedFlow<Location> = _locationUpdates

    /** StateFlow to track whether location tracking is active. */
    private val _trackingState = MutableStateFlow(false)
    override val trackingState: StateFlow<Boolean> = _trackingState

    /** Listener for receiving location updates from the Location Manager. */
    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            // Emit the new location to the Flow
            _locationUpdates.tryEmit(location)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    /**
     * Initializes location tracking by setting tracking state to active.
     */
    override suspend fun initializeTracking() {
        // Initialize tracking logic here
        _trackingState.value = true
    }

    /**
     * Checks and requests necessary location permissions.
     *
     * @return `true` if permissions are granted, `false` otherwise.
     */
    override suspend fun checkAndRequestPermissions(): Boolean {
        // Check and request location permissions
        // Return true if permissions are granted, false otherwise
        return true // Replace with actual permission check logic
    }

    /**
     * Starts continuous location tracking if permissions are granted.
     */
    override suspend fun startContinuousTracking() {
        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(
                context, // Use the injected context here
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context, // Use the injected context here
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permissions are not granted, handle this case
            // For example, you can log an error or throw an exception
            println("Location permissions are not granted.")
            return
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000L, // Update interval in milliseconds
            1f, // Minimum distance in meters
            locationListener,
            Looper.getMainLooper()
        )
    }

    /**
     * Retrieves the last known location if permissions are granted.
     *
     * @return The last known [Location], or `null` if unavailable.
     */
    @SuppressLint("MissingPermission")
    override suspend fun getLastKnownLocation(): Location? {
        return if (checkAndRequestPermissions()) {
            fusedLocationClient.lastLocation.await()
        } else {
            null
        }
    }

    /**
     * Cleans up resources by removing location updates and setting tracking state to inactive.
     */
    override fun cleanup() {
        // Clean up resources, e.g., remove location updates
        locationManager.removeUpdates(locationListener)
        _trackingState.value = false
    }
}