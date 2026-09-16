package com.example.weatherapp.domain.repository

import com.example.weatherapp.domain.model.Coordinates

interface LocationRepository {
    suspend fun getCurrentLocation(): Result<Coordinates>
}
