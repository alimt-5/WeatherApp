package com.example.weatherapp.domain.location

sealed interface LocationResult {
    data class Success(val latitude: Double, val longitude: Double) : LocationResult
    data object LocationDisabled : LocationResult
    data object PermissionDenied : LocationResult
    data object Unavailable : LocationResult
}