package com.practicum.playlistmaker_ver2.favorite.domain.repositories

import com.practicum.playlistmaker_ver2.favorite.data.db.entity.FavoriteEntity
import com.practicum.playlistmaker_ver2.favorite.domain.interactors.FavoriteInteractor
import com.practicum.playlistmaker_ver2.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteInteractorImpl(private val repository: FavoriteRepository) : FavoriteInteractor {
    override suspend fun insertFavoriteTrack(track: FavoriteEntity) {
        return repository.insertFavoriteTrack(track)
    }

    override suspend fun deleteFavoriteTrack(track: FavoriteEntity) {
        return repository.deleteFavoriteTrack(track)
    }

    override suspend fun getFavoriteTrack(): Flow<List<Track>> {
        return repository.getFavoriteTrack()
    }

    override suspend fun getFavoriteTrackId(): Flow<List<Long>> {
        return repository.getFavoriteTrackId()
    }
}

