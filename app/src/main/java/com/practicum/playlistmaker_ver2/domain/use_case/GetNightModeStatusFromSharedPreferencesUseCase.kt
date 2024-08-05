package com.practicum.playlistmaker_ver2.domain.use_case

import com.practicum.playlistmaker_ver2.domain.repository.NightModeRepositoryInSharedPreferences

class GetNightModeStatusFromSharedPreferencesUseCase(private val nightModeRepositoryInSharedPreferences: NightModeRepositoryInSharedPreferences) {
    fun execute(): Boolean {
        return nightModeRepositoryInSharedPreferences.getNightModeStatusInSharedPreferencesUseCase()
    }
}