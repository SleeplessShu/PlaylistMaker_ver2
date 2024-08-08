package com.practicum.playlistmaker_ver2.domain.use_case

import com.practicum.playlistmaker_ver2.domain.models.Track
import com.practicum.playlistmaker_ver2.domain.repository.ClickedTracksRepository

class AddClickedTrackUseCase(private val sharedPreferencesRepository: ClickedTracksRepository) {
    fun execute(track: Track) {
        sharedPreferencesRepository.addClickedTrack(track)
    }
}