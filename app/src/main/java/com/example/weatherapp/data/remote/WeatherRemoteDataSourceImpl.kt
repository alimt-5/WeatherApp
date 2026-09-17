package com.example.weatherapp.data.remote

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.remote.model.WeatherDto
import javax.inject.Inject

class WeatherRemoteDataSourceImpl @Inject constructor(
    private val api: WeatherApiService
) : WeatherRemoteDataSource {
    override suspend fun getCurrentWeather(query: String): WeatherDto {
        check(BuildConfig.WEATHER_API_KEY.isNotBlank()) {
            "WEATHER_API_KEY is missing. Add it to local.properties."
        }
        return api.getCurrentWeather(BuildConfig.WEATHER_API_KEY, query)
    }
}
