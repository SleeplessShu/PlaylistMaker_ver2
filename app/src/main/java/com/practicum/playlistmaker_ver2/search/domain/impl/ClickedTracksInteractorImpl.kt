package com.practicum.playlistmaker_ver2.search.domain.impl

import com.practicum.playlistmaker_ver2.search.domain.interactor.ClickedTracksInteractor
import com.practicum.playlistmaker_ver2.search.domain.models.Track
import com.practicum.playlistmaker_ver2.search.domain.repository.ClickedTracksRepository

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



