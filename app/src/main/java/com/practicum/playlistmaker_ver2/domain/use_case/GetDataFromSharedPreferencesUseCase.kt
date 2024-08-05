package com.practicum.playlistmaker_ver2.domain.use_case

import com.practicum.playlistmaker_ver2.domain.models.Track
import com.practicum.playlistmaker_ver2.domain.repository.TracksRepositorySharedPreferences

class GetDataFromSharedPreferencesUseCase(private val sharedPreferencesRepository: TracksRepositorySharedPreferences) {

    fun execute(): List<Track> {
        return sharedPreferencesRepository.getDataFromSharedPreferences()
    }
}
