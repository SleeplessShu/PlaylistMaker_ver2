package com.practicum.playlistmaker_ver2.favorite.domain.repositories

import com.practicum.playlistmaker_ver2.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import com.practicum.playlistmaker_ver2.favorite.data.db.entity.FavoriteEntity

interface FavoriteRepository {
    suspend fun insertFavoriteTrack(track:FavoriteEntity)
    suspend fun deleteFavoriteTrack(track:FavoriteEntity)
    suspend fun getFavoriteTrack(): Flow<List<Track>>

    suspend fun getFavoriteTrackId(): Flow<List<Long>>



}