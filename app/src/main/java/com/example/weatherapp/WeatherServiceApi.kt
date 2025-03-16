package com.example.weatherapp

import com.example.weatherapp.models.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
interface WeatherServiceApi {
    @GET("current.json")
    fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") location: String
    ): Call<WeatherResponse>
}