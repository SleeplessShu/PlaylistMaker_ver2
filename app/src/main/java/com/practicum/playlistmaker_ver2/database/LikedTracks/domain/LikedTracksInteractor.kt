package com.practicum.playlistmaker_ver2.database.LikedTracks.domain

import com.practicum.playlistmaker_ver2.player.ui.models.PlayerTrack
import com.practicum.playlistmaker_ver2.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface LikedTracksInteractor {
    fun getTracks(): Flow<List<Track>>
    suspend fun addTrack(playerTrack: PlayerTrack)
    suspend fun deleteEntity(playerTrack: PlayerTrack)
}