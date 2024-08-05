package com.practicum.playlistmaker_ver2.domain.use_case

import com.practicum.playlistmaker_ver2.domain.repository.NightModeRepository

class GetNightModeStatusUseCase(private val nightModeRepositoryInSharedPreferences: NightModeRepository) {
    fun execute(): Boolean {
        return nightModeRepositoryInSharedPreferences.getNightModeStatus()
    }
}