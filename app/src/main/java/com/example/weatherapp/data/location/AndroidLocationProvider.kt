package com.example.weatherapp.data.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.example.weatherapp.domain.location.LocationProvider
import com.example.weatherapp.domain.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class AndroidLocationProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : LocationProvider {

    private val locationClient = LocationServices.getFusedLocationProviderClient(context)

    override suspend fun getCurrentLocation(): LocationResult {
        val fineGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!fineGranted && !coarseGranted) {
            return LocationResult.PermissionDenied
        }

        val locationManager = context.getSystemService(LocationManager::class.java)

        val locationEnabled =
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (!locationEnabled) {
            return LocationResult.LocationDisabled
        }

        return suspendCancellableCoroutine { continuation ->
            locationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null
            ).addOnSuccessListener { location ->
                if (location != null) {
                    continuation.resume(
                        LocationResult.Success(
                            latitude = location.latitude,
                            longitude = location.longitude
                        )
                    )
                } else {
                    continuation.resume(LocationResult.Unavailable)
                }
            }.addOnFailureListener {
                continuation.resume(LocationResult.Unavailable)
            }
        }
    }
}