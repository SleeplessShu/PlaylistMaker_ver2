package com.practicum.playlistmaker_ver2.domain.use_case

import com.practicum.playlistmaker_ver2.domain.models.Track
import com.practicum.playlistmaker_ver2.domain.repository.TracksRepositoryInSharedPreferences

class PutDataToSharedPreferencesUseCase(private val sharedPreferencesRepository: TracksRepositoryInSharedPreferences) {
    fun execute(track: Track): Boolean {
        return true
    }
}