package com.example.weatherapp.domain.model

data class CurrentWeather(
    val temperatureCelsius: Double,
    val feelsLikeCelsius: Double,
    val condition: String,
    val iconUrl: String,
    val windKph: Double,
    val pressureMb: Double,
    val humidityPercent: Int,
    val isDay: Boolean
)
