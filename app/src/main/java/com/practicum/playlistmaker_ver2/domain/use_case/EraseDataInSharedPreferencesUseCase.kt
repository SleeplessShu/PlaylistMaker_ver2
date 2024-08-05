package com.practicum.playlistmaker_ver2.domain.use_case

import com.practicum.playlistmaker_ver2.domain.repository.TracksRepositoryInSharedPreferences

class EraseDataInSharedPreferencesUseCase(private val sharedPreferencesRepository: TracksRepositoryInSharedPreferences) {
    fun execute() {
        sharedPreferencesRepository.eraseDataInSharedPreferences()
    }
}