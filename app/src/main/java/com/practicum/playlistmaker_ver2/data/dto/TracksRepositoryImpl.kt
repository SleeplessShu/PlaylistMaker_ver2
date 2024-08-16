package com.practicum.playlistmaker_ver2.data.dto

import android.util.Log
import com.practicum.playlistmaker_ver2.data.NetworkClient
import com.practicum.playlistmaker_ver2.data.mapper.TrackDtoToTrackMapper
import com.practicum.playlistmaker_ver2.domain.api.TracksRepository
import com.practicum.playlistmaker_ver2.domain.models.Track
import com.practicum.playlistmaker_ver2.util.Resource


class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        val msg: String = response.resultCode.toString() + " " + response.toString()
        Log.d("DEBUG", msg)

        return when (response.resultCode) {
            200 -> {
                Resource.Success(TrackDtoToTrackMapper.map((response as TrackSearchResponse).results))
            }
            else -> {
                Resource.Error(response.resultCode.toString())
            }
        }
    }
}