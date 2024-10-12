package com.practicum.playlistmaker_ver2.search.domain.impl

import com.practicum.playlistmaker_ver2.search.domain.interactor.TrackInteractor
import com.practicum.playlistmaker_ver2.search.domain.repository.TracksRepository
import com.practicum.playlistmaker_ver2.search.data.dto.Resource
import com.practicum.playlistmaker_ver2.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class TrackInteractorImpl(
    private val repository: TracksRepository
) : TrackInteractor {

    override fun doRequest(expression: String): Flow<Pair<List<Track>?, String?>> {
        return repository.searchTracks(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }
}

