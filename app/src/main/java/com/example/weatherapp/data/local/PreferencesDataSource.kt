package com.example.weatherapp.data.local

import kotlinx.coroutines.flow.Flow

interface PreferencesDataSource {
    val savedQuery: Flow<String>
    suspend fun saveQuery(query: String)
}
