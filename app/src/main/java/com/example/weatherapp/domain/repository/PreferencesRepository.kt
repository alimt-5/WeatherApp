package com.example.weatherapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val savedQuery: Flow<String>;
    suspend fun saveQuery(query: String)
}
