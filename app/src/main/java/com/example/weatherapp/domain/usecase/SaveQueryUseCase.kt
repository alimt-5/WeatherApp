package com.example.weatherapp.domain.usecase

import com.example.weatherapp.domain.repository.PreferencesRepository
import javax.inject.Inject

class SaveQueryUseCase @Inject constructor(
    private val repository: PreferencesRepository
) {
    suspend operator fun invoke(query: String) = repository.saveQuery(query)
}
