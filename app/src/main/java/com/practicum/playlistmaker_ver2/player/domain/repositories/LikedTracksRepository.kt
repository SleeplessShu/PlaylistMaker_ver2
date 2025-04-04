package com.practicum.playlistmaker_ver2.player.domain.repositories

import com.practicum.playlistmaker_ver2.player.ui.models.PlayerTrack
import com.practicum.playlistmaker_ver2.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface LikedTracksRepository {

    fun getTracksFromDb(): Flow<List<Track>>
    suspend fun addTrackToDb(playerTrack: PlayerTrack)
    suspend fun deleteEntityFromDb(playerTrack: PlayerTrack)
}