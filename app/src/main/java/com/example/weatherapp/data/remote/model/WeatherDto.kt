package com.example.weatherapp.data.remote.model

data class LocationDto(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val tz_id: String,
    val localtime: String
)

data class CurrentDto(
    val temp_c: Double,
    val condition: ConditionDto,
    val wind_kph: Double,
    val pressure_mb: Double,
    val humidity: Int,
    val feelslike_c: Double,
    val is_day: Int
)

data class WeatherDto(
    val location: LocationDto,
    val current: CurrentDto
)

data class ConditionDto(
    val text: String,
    val icon: String,
    val code: Int
)
