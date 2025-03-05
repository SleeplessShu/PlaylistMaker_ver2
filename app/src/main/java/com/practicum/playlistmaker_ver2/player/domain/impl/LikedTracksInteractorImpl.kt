package com.practicum.playlistmaker_ver2.player.domain.impl

import com.practicum.playlistmaker_ver2.player.domain.api.LikedTracksInteractor
import com.practicum.playlistmaker_ver2.player.domain.repositories.LikedTracksRepository
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerTrack
import com.practicum.playlistmaker_ver2.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class LikedTracksInteractorImpl(
    private val likedTracksRepository: LikedTracksRepository
) : LikedTracksInteractor {
    override fun getTracks(): Flow<List<Track>> {
        return likedTracksRepository.getTracksFromDb()
    }

    override suspend fun addTrack(playerTrack: PlayerTrack) {
        likedTracksRepository.addTrackToDb(playerTrack)
    }

    override suspend fun deleteEntity(playerTrack: PlayerTrack) {
        likedTracksRepository.deleteEntityFromDb(playerTrack)
    }
}