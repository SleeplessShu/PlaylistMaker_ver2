package com.practicum.playlistmaker_ver2.search.domain.interactor


import com.practicum.playlistmaker_ver2.search.data.dto.Resource
import com.practicum.playlistmaker_ver2.search.domain.models.ResponsePair
import com.practicum.playlistmaker_ver2.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {
    fun doRequest(expression: String): Flow<Pair<List<Track>?, String?>>
}