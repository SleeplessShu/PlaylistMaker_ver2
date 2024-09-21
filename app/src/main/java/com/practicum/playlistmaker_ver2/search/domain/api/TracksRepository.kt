package com.practicum.playlistmaker_ver2.search.domain.api


import com.practicum.playlistmaker_ver2.search.domain.models.Track
import com.practicum.playlistmaker_ver2.search.domain.models.Resource


interface TracksRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
}