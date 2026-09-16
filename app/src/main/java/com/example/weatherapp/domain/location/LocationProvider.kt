package com.example.weatherapp.domain.location

interface LocationProvider {
    suspend fun getCurrentLocation(): LocationResult
}