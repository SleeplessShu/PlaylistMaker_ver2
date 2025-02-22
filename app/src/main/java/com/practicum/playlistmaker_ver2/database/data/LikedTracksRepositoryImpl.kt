package com.practicum.playlistmaker_ver2.database.data


import com.practicum.playlistmaker_ver2.database.data.converters.TrackDbConverter
import com.practicum.playlistmaker_ver2.database.domain.LikedTracksRepository
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerTrack
import com.practicum.playlistmaker_ver2.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class LikedTracksRepositoryImpl(
    private val likedTracksDatabase: LikedTracksDatabase,
    private val trackDbConverter: TrackDbConverter
) : LikedTracksRepository {
    override fun getTracksFromDb(): Flow<List<Track>> {
        return likedTracksDatabase
            .getTrackDao()
            .getAllTracks()
            .map { trackEntities ->
                convertFromTrackEntity(trackEntities)
            }
    }

    override suspend fun addTrackToDb(playerTrack: PlayerTrack) {
        val entity = convertToTrackEntity(playerTrack)
        val minOrder = likedTracksDatabase
            .getTrackDao()
            .getMinOrder() ?: 0

        val newOrder = minOrder - 1

        val entityWithOrder = entity.copy(order = newOrder)
        likedTracksDatabase
            .getTrackDao()
            .addTrackToLikedDb(entityWithOrder)
    }

    override suspend fun deleteEntityFromDb(playerTrack: PlayerTrack) {
        val entity = convertToTrackEntity(playerTrack)
        likedTracksDatabase.getTrackDao().deleteTrackEntity(entity)
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConverter.mapToList(track) }
    }

    private fun convertToTrackEntity(track: PlayerTrack): TrackEntity {
        return trackDbConverter.mapToTrackEntity(track)
    }


}

