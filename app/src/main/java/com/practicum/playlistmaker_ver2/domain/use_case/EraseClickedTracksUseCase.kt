package com.practicum.playlistmaker_ver2.domain.use_case

import com.practicum.playlistmaker_ver2.domain.repository.ClickedTracksRepository

class EraseClickedTracksUseCase(private val sharedPreferencesRepository: ClickedTracksRepository) {
    fun execute() {
        sharedPreferencesRepository.eraseClickedTracks()
    }
}