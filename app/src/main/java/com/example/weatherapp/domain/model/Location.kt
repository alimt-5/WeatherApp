package com.example.weatherapp.domain.model

data class Location(
    val name: String,
    val region: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val localTime: String
)
