package com.practicum.playlistmaker_ver2.domain.use_case

import com.practicum.playlistmaker_ver2.domain.models.Track
import com.practicum.playlistmaker_ver2.domain.repository.ClickedTracksRepository

class GetClickedTracksUseCase(private val sharedPreferencesRepository: ClickedTracksRepository) {

    fun execute(): List<Track> {
        return sharedPreferencesRepository.getClickedTracks()
    }
}
