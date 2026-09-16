package com.example.weatherapp.core.di

import com.example.weatherapp.core.network.NetworkConfig
import com.example.weatherapp.data.remote.WeatherApiService
import com.example.weatherapp.data.remote.WeatherRemoteDataSource
import com.example.weatherapp.data.remote.WeatherRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder().baseUrl(NetworkConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()

    @Provides
    @Singleton
    fun provideWeatherApi(retrofit: Retrofit): WeatherApiService =
        retrofit.create(WeatherApiService::class.java)

    @Provides
    @Singleton
    fun provideRemoteDataSource(impl: WeatherRemoteDataSourceImpl): WeatherRemoteDataSource = impl
}
