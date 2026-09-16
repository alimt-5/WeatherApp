package com.example.weatherapp.data.repository

import com.example.weatherapp.data.local.PreferencesDataSource
import com.example.weatherapp.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val dataSource: PreferencesDataSource
) : PreferencesRepository {
    override val savedQuery: Flow<String> = dataSource.savedQuery
    override suspend fun saveQuery(query: String) {
        dataSource.saveQuery(query)
    }
}
