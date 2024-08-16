package com.practicum.playlistmaker_ver2.domain.impl

import com.practicum.playlistmaker_ver2.domain.interactor.ClickedTracksInteractor
import com.practicum.playlistmaker_ver2.domain.models.Track
import com.practicum.playlistmaker_ver2.domain.repository.ClickedTracksRepository

class ClickedTracksInteractorImpl(
    private val repository: ClickedTracksRepository
) : ClickedTracksInteractor {
    private companion object {

    }

    override
    fun getTracks(): List<Track> {
        return repository.getTracks()
    }

    override
    fun eraseTracks() {
        repository.eraseTracks()
    }

    override
    fun addTrack(track: Track) {
        repository.addTrack(track)
    }
}



