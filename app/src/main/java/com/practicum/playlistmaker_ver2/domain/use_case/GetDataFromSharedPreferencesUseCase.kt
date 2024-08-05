package com.practicum.playlistmaker_ver2.domain.use_case

import com.practicum.playlistmaker_ver2.domain.models.Track
import com.practicum.playlistmaker_ver2.domain.repository.TracksRepositoryInSharedPreferences

class GetDataFromSharedPreferencesUseCase(private val sharedPreferencesRepository: TracksRepositoryInSharedPreferences) {

    fun execute(): List<Track> {
        return sharedPreferencesRepository.getDataFromSharedPreferences()
    }
}
