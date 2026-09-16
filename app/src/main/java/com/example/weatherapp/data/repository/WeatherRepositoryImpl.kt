package com.example.weatherapp.data.repository

import com.example.weatherapp.data.remote.WeatherRemoteDataSource
import com.example.weatherapp.data.remote.toDomain
import com.example.weatherapp.domain.model.Weather
import com.example.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.CancellationException
import java.io.IOException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val remoteDataSource: WeatherRemoteDataSource) :
    WeatherRepository {
    override suspend fun getCurrentWeather(query: String): Result<Weather> {
        if (query.isBlank()) return Result.failure(IllegalArgumentException("Enter a city name."))
        return try {
            Result.success(remoteDataSource.getCurrentWeather(query).toDomain())
        } catch (error: CancellationException) {
            throw error
        } catch (error: IOException) {
            Result.failure(IOException("Network request failed. Check your connection.", error))
        } catch (error: Throwable) {
            Result.failure(error)
        }
    }
}
