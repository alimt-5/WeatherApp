package com.example.weatherapp.data.di

import com.example.weatherapp.data.location.AndroidLocationProvider
import com.example.weatherapp.domain.location.LocationProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Binds
    @Singleton
    abstract fun bindLocationProvider(
        implementation: AndroidLocationProvider
    ): LocationProvider
}