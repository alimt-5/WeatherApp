package com.example.weatherapp.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PreferencesDataSource {
    private val queryKey = stringPreferencesKey("saved_query")
    override val savedQuery: Flow<String> = dataStore.data.map { it[queryKey].orEmpty() }
    override suspend fun saveQuery(query: String) {
        dataStore.edit { it[queryKey] = query }
    }
}
