package com.example.weatherapp.data.remote

import com.example.weatherapp.data.remote.model.WeatherDto

interface WeatherRemoteDataSource {
    suspend fun getCurrentWeather(query: String): WeatherDto
}
