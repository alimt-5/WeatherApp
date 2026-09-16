package com.example.weatherapp.domain.usecase

import com.example.weatherapp.domain.repository.PreferencesRepository
import javax.inject.Inject

class ObserveSavedQueryUseCase @Inject constructor(
    private val repository: PreferencesRepository
) {
    operator fun invoke() = repository.savedQuery
}
