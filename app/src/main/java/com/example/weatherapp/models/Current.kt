package com.example.weatherapp.models
data class Current(
    val temp_c: Double,
    val condition: Condition,
    val wind_kph: Double,
    val pressure_mb : Double,
    val humidity: Int,
    val feelslike_c : Double
)
