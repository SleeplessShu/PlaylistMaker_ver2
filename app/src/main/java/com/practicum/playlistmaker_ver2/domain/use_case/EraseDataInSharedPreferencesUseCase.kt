package com.practicum.playlistmaker_ver2.domain.use_case

import com.practicum.playlistmaker_ver2.domain.repository.TracksRepositorySharedPreferences

class EraseDataInSharedPreferencesUseCase(private val sharedPreferencesRepository: TracksRepositorySharedPreferences) {
    fun execute() {
        sharedPreferencesRepository.eraseDataInSharedPreferences()
    }
}