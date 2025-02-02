package com.practicum.playlistmaker_ver2.favorite.data.repository

import com.practicum.playlistmaker_ver2.AppDataBase
import com.practicum.playlistmaker_ver2.favorite.data.db.converters.FavoriteTrackDatabaseConverter
import com.practicum.playlistmaker_ver2.favorite.data.db.entity.FavoriteEntity
import com.practicum.playlistmaker_ver2.favorite.domain.repositories.FavoriteRepository
import com.practicum.playlistmaker_ver2.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class FavoriteRepositoryImpl(
    private val appDataBase: AppDataBase,
    private val favoriteTrackDbConverter: FavoriteTrackDatabaseConverter,
): FavoriteRepository {
    override suspend fun insertFavoriteTrack(track: FavoriteEntity) {
        appDataBase.favoriteDao().insertTrack(track)
    }

    override suspend fun deleteFavoriteTrack(track:FavoriteEntity) {
        appDataBase.favoriteDao().deleteTrackEntity(track)

    }

    override suspend fun getFavoriteTrack(): Flow<List<Track>> = flow {
        val tracks = appDataBase.favoriteDao().sortTrack()

        emit(convertFromFavoriteEntity(tracks))
    }


    override suspend fun getFavoriteTrackId(): Flow<List<Long>> = flow {
        val tracks =appDataBase.favoriteDao().getTracksIds()
        emit(tracks)
    }



    private fun convertFromFavoriteEntity(tracks:List<FavoriteEntity>): List<Track>{
        return tracks.map { track -> favoriteTrackDbConverter.map(track)}
    }
}
