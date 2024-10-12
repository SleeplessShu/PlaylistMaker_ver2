package com.practicum.playlistmaker_ver2.search.data.dto

import android.util.Log
import com.practicum.playlistmaker_ver2.search.data.network.NetworkClient
import com.practicum.playlistmaker_ver2.search.data.mapper.TrackDtoToTrackMapper
import com.practicum.playlistmaker_ver2.search.domain.repository.TracksRepository
import com.practicum.playlistmaker_ver2.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow



class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        val msg: String = response.resultCode.toString() + " " + response.toString()
        Log.d("DEBUG", msg)

        when (response.resultCode) {
            200 -> {
                with(response as TrackSearchResponse) {
                    val data = TrackDtoToTrackMapper.map(results)
                    emit(Resource.Success(data))
                }
            }

            else -> {
                emit(Resource.Error(response.resultCode.toString(), null))
            }
        }
    }
}