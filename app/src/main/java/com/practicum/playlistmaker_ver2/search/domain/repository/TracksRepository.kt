package com.practicum.playlistmaker_ver2.search.domain.repository


import com.practicum.playlistmaker_ver2.search.domain.models.Track
import com.practicum.playlistmaker_ver2.search.data.dto.Resource
import kotlinx.coroutines.flow.Flow


interface TracksRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
}