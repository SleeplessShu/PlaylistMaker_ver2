package com.practicum.playlistmaker_ver2.domain.api


import com.practicum.playlistmaker_ver2.domain.models.Track
import com.practicum.playlistmaker_ver2.util.Resource

interface TracksRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
}