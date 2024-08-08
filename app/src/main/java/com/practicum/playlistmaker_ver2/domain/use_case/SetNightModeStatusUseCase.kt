package com.practicum.playlistmaker_ver2.domain.use_case

import com.practicum.playlistmaker_ver2.domain.repository.NightModeRepository

class SetNightModeStatusUseCase(private val nightModeRepositoryInSharedPreferences: NightModeRepository) {
    fun execute(currentStatus: Boolean) {
        nightModeRepositoryInSharedPreferences.setNightModeStatus(currentStatus)
    }
}