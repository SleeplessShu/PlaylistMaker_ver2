package com.practicum.playlistmaker_ver2.domain.interactor

import com.practicum.playlistmaker_ver2.domain.models.Track


interface ClickedTracksInteractor {
    fun getTracks(): List<Track>

    fun eraseTracks()

    fun addTrack(track: Track)
}
