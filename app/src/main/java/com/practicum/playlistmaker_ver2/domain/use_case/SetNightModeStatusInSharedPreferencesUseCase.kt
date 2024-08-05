package com.practicum.playlistmaker_ver2.domain.use_case

import com.practicum.playlistmaker_ver2.domain.repository.NightModeRepositoryInSharedPreferences
import com.practicum.playlistmaker_ver2.domain.repository.TracksRepositoryInSharedPreferences

class SetNightModeStatusInSharedPreferencesUseCase(private val nightModeRepositoryInSharedPreferences: NightModeRepositoryInSharedPreferences) {
    fun execute(status: Boolean) {
        nightModeRepositoryInSharedPreferences.getNightModeStatusInSharedPreferencesUseCase()
    }
}