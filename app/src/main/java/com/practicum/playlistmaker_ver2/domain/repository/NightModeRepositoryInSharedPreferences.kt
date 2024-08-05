package com.practicum.playlistmaker_ver2.domain.repository

interface NightModeRepositoryInSharedPreferences {
    fun setNightModeStatusInSharedPreferencesUseCase(currentStatus: Boolean)
    fun getNightModeStatusInSharedPreferencesUseCase(): Boolean
}