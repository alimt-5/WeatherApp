package com.example.weatherapp.domain.usecase

import com.example.weatherapp.domain.repository.LocationRepository
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    suspend operator fun invoke() = repository.getCurrentLocation()
}
