package com.practicum.playlistmaker_ver2.old.domain.api


import com.practicum.playlistmaker_ver2.old.domain.models.Track
import com.practicum.playlistmaker_ver2.old.domain.models.Resource


interface TracksRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
}