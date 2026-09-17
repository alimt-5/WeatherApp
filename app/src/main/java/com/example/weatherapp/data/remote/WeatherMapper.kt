package com.example.weatherapp.data.remote

import com.example.weatherapp.data.remote.model.WeatherDto
import com.example.weatherapp.domain.model.CurrentWeather
import com.example.weatherapp.domain.model.Location
import com.example.weatherapp.domain.model.Weather

fun WeatherDto.toDomain(): Weather = Weather(
    location = Location(
        name = location.name,
        region = location.region,
        country = location.country,
        latitude = location.lat,
        longitude = location.lon,
        localTime = location.localtime
    ),
    current = CurrentWeather(
        temperatureCelsius = current.temp_c,
        feelsLikeCelsius = current.feelslike_c,
        condition = current.condition.text,
        iconUrl = current.condition.icon.toIconUrl(),
        windKph = current.wind_kph,
        pressureMb = current.pressure_mb,
        humidityPercent = current.humidity,
        isDay = current.is_day == 1
    )
)

private fun String.toIconUrl(): String =
    if (startsWith("//")) "https:$this" else this
