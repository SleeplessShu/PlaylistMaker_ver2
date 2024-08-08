package com.practicum.playlistmaker_ver2.domain.repository

import com.practicum.playlistmaker_ver2.domain.models.Track


interface ClickedTracksRepository {
    fun addClickedTrack(track: Track)
    fun getClickedTracks(): List<Track>
    fun eraseClickedTracks()
}