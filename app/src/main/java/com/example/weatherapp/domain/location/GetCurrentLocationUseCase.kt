package com.example.weatherapp.domain.location

import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val locationProvider: LocationProvider
) {
    suspend operator fun invoke(): LocationResult {
        return locationProvider.getCurrentLocation()
    }
}