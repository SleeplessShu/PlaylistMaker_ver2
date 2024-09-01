package com.practicum.playlistmaker_ver2.old.domain.impl

import com.practicum.playlistmaker_ver2.old.domain.interactor.ClickedTracksInteractor
import com.practicum.playlistmaker_ver2.old.domain.models.Track
import com.practicum.playlistmaker_ver2.old.domain.repository.ClickedTracksRepository

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



