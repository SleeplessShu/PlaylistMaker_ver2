package com.practicum.playlistmaker_ver2.search.domain.interactor

import com.practicum.playlistmaker_ver2.search.domain.models.Track


interface ClickedTracksInteractor {
    fun getTracks(): List<Track>

    fun eraseTracks()

    fun addTrack(track: Track)
}
