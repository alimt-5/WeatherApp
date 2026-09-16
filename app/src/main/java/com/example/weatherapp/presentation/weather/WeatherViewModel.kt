package com.example.weatherapp.presentation.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.domain.location.LocationDisabledException
import com.example.weatherapp.domain.model.Coordinates
import com.example.weatherapp.domain.usecase.GetCurrentLocationUseCase
import com.example.weatherapp.domain.usecase.GetCurrentWeatherUseCase
import com.example.weatherapp.domain.usecase.ObserveSavedQueryUseCase
import com.example.weatherapp.domain.usecase.SaveQueryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCurrentWeather: GetCurrentWeatherUseCase,
    private val getCurrentLocation: GetCurrentLocationUseCase,
    private val observeSavedQuery: ObserveSavedQueryUseCase,
    private val saveQuery: SaveQueryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<WeatherUiState>(
        WeatherUiState.Loading
    )

    val state: StateFlow<WeatherUiState> = _state.asStateFlow()

    private var query = ""
    private var lastWeatherQuery = ""

    init {
        loadSavedQuery()
    }

    fun onEvent(event: WeatherEvent) {
        when (event) {
            is WeatherEvent.QueryChanged -> {
                query = event.query
            }

            WeatherEvent.Search -> {
                search()
            }

            WeatherEvent.Refresh -> {
                refresh()
            }

            WeatherEvent.UseCurrentLocation -> {
                loadFromLocation()
            }

            WeatherEvent.DismissError -> {
                dismissError()
            }
        }
    }

    fun onLocationPermissionResult(granted: Boolean) {
        if (granted) {
            loadFromLocation()
        } else {
            _state.value = WeatherUiState.Error(
                message = "Location permission was not granted.",
                query = query
            )
        }
    }

    private fun loadSavedQuery() {
        viewModelScope.launch {
            query = observeSavedQuery().first()

            if (query.isBlank()) {
                _state.value = WeatherUiState.Error(
                    message = "Search for a city to get the weather.",
                    query = ""
                )
            } else {
                loadWeather(query)
            }
        }
    }

    private fun search() {
        val normalizedQuery = query.trim()

        if (normalizedQuery.isBlank()) {
            _state.value = WeatherUiState.Error(
                message = "Enter a city name.",
                query = query
            )
            return
        }

        viewModelScope.launch {
            saveQuery(normalizedQuery)
            loadWeather(normalizedQuery)
        }
    }

    private fun refresh() {
        val currentQuery = lastWeatherQuery.ifBlank {
            query.trim()
        }

        if (currentQuery.isNotBlank()) {
            viewModelScope.launch {
                loadWeather(currentQuery)
            }
        }
    }

    private fun loadFromLocation() {
        viewModelScope.launch {
            _state.value = WeatherUiState.Loading

            getCurrentLocation()
                .onSuccess { coordinates ->
                    loadWeatherByCoordinates(coordinates)
                }
                .onFailure { error ->
                    handleLocationError(error)
                }
        }
    }

    private suspend fun loadWeatherByCoordinates(
        coordinates: Coordinates
    ) {
        val coordinateQuery =
            "${coordinates.latitude},${coordinates.longitude}"

        getCurrentWeather(coordinateQuery)
            .onSuccess { weather ->
                lastWeatherQuery = coordinateQuery
                query = weather.location.name
                saveQuery(weather.location.name)

                _state.value = WeatherUiState.Success(
                    weather = weather,
                    query = weather.location.name
                )
            }
            .onFailure { error ->
                _state.value = WeatherUiState.Error(
                    message = error.message
                        ?: "Unable to load weather for your location.",
                    query = query
                )
            }
    }

    private suspend fun loadWeather(searchQuery: String) {
        query = searchQuery.trim()

        _state.value = WeatherUiState.Loading

        getCurrentWeather(query)
            .onSuccess { weather ->
                lastWeatherQuery = query

                _state.value = WeatherUiState.Success(
                    weather = weather,
                    query = query
                )
            }
            .onFailure { error ->
                _state.value = WeatherUiState.Error(
                    message = error.message
                        ?: "Unable to load weather.",
                    query = query
                )
            }
    }

    private fun handleLocationError(error: Throwable) {
        when (error) {
            is LocationDisabledException -> {
                _state.value = WeatherUiState.Error(
                    message = "Location services are disabled. Enable them from Settings.",
                    query = query,
                    isLocationDisabled = true
                )
            }

            else -> {
                _state.value = WeatherUiState.Error(
                    message = error.message
                        ?: "Unable to get your current location.",
                    query = query
                )
            }
        }
    }

    private fun dismissError() {
        if (lastWeatherQuery.isNotBlank()) {
            viewModelScope.launch {
                loadWeather(lastWeatherQuery)
            }
        }
    }
}