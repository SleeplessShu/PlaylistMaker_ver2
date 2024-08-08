package com.practicum.playlistmaker_ver2.data.dto

import android.util.Log
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.data.NetworkClient
import com.practicum.playlistmaker_ver2.data.mapper.TrackDtoToTrackMapper
import com.practicum.playlistmaker_ver2.domain.api.TracksRepository
import com.practicum.playlistmaker_ver2.domain.models.Track
import com.practicum.playlistmaker_ver2.util.Resource

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        Log.d("shu", "TracksRepositoryImpl -> ${response.resultCode}")
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("${R.string.network_error1}")
            }

            200 -> {
                Resource.Success(TrackDtoToTrackMapper.map((response as TrackSearchResponse).results))
            }

            else -> {
                Resource.Error("${R.string.network_error2}")
            }
        }
    }
}