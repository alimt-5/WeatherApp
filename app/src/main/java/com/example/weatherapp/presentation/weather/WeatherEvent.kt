package com.example.weatherapp.presentation.weather

sealed interface WeatherEvent {
    data class QueryChanged(val query: String) : WeatherEvent
    data object Search : WeatherEvent
    data object Refresh : WeatherEvent
    data object UseCurrentLocation : WeatherEvent
    data object DismissError : WeatherEvent
}
