package com.example.weatherapp.presentation.weather

import com.example.weatherapp.domain.model.Weather

sealed interface WeatherUiState {
    data object Loading : WeatherUiState
    data class Success(val weather: Weather, val query: String) : WeatherUiState
    data class Error(val message: String, val query: String, val isLocationDisabled: Boolean = false) : WeatherUiState
}