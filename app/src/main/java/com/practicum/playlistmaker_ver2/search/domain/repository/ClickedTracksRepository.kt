package com.practicum.playlistmaker_ver2.search.domain.repository

import com.practicum.playlistmaker_ver2.search.domain.models.Track


interface ClickedTracksRepository {
    fun addTrack(track: Track)
    fun getTracks(): List<Track>
    fun eraseTracks()
}