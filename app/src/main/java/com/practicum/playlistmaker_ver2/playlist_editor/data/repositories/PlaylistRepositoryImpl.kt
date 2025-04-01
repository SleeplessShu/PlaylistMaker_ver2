package com.practicum.playlistmaker_ver2.playlist_editor.data.repositories

import android.net.Uri
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerTrack
import com.practicum.playlistmaker_ver2.playlist_editor.data.entities.PlaylistEntity
import com.practicum.playlistmaker_ver2.playlist_editor.data.entities.TrackInPlaylistEntity
import com.practicum.playlistmaker_ver2.playlist_editor.domain.PlaylistRepository
import com.practicum.playlistmaker_ver2.playlist_editor.domain.models.PlaylistEntityPresentation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val tracksInPlaylistsDao: TracksInPlaylistsDao

) : PlaylistRepository {

    override suspend fun addOrUpdateTrackInPlaylist(
        playlistId: Int,
        track: PlayerTrack
    ): Result<Unit> {
        return try {
            val existingTrack =
                runCatching { tracksInPlaylistsDao.getTrackById(track.trackId) }.getOrNull()

            if (existingTrack != null) {
                val updatedPlaylists =
                    existingTrack.playlistsIDs.split(",").map { it.trim() }.toMutableSet()
                updatedPlaylists += playlistId.toString()
                val updatedEntity =
                    existingTrack.copy(playlistsIDs = updatedPlaylists.joinToString(","))
                tracksInPlaylistsDao.updateTrack(updatedEntity)
            } else {
                val entity = track.toData(playlistId)
                tracksInPlaylistsDao.addTrackToTracksInPlaylists(entity)
            }

            playlistDao.addTrackToPlaylist(playlistId, track.trackId.toString())

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updatePlaylist(
        playlistID: Int,
        image: Uri,
        title: String,
        description: String
    ) {
        val playlist = playlistDao.getAllPlaylists().map { playlists ->
            playlists.find { it.id == playlistID }
        }.firstOrNull()

        playlist?.let {
            val updatedPlaylist = it.copy(
                name = title,
                image = image.toString(),
                description = description
            )
            playlistDao.updatePlaylist(updatedPlaylist)
        }
    }

    override suspend fun addPlaylist(image: Uri, title: String, description: String) {
        val entity = PlaylistEntity(
            name = title,
            description = description,
            image = image.toString(),
        )
        playlistDao.addPlaylist(entity)
    }

    override suspend fun removeTrack(playlistID: Int, trackID: Int) {
        playlistDao.removeTrackFromPlaylist(playlistID, trackID.toString())
        removePlaylistIdFromTrack(trackID, playlistID)
        val isEmpty = isPlaylistsIDsEmpty(trackID)
        if (isEmpty) {
            cleanupTrackIfNotInAnyPlaylist(trackID)
        }
    }

    suspend fun isPlaylistsIDsEmpty(trackId: Int): Boolean {
        val playlists = runCatching {
            tracksInPlaylistsDao.getPlaylistsIDsByTrackId(trackId)
        }.getOrNull() ?: return true

        return playlists.trim().isEmpty()
    }


    suspend fun cleanupTrackIfNotInAnyPlaylist(trackID: Int) {
        val track = runCatching { tracksInPlaylistsDao.getTrackById(trackID) }.getOrNull()
        track?.let {
            tracksInPlaylistsDao.deleteTrackEntity(it)
        }
    }

    suspend fun removePlaylistIdFromTrack(trackId: Int, playlistIdToRemove: Int) {
        val entity = tracksInPlaylistsDao.getTrackById(trackId)
        val updatedPlaylists = entity.playlistsIDs.split(",").map { it.trim() }
            .filter { it.isNotEmpty() && it != playlistIdToRemove.toString() }.joinToString(",")

        val updatedEntity = entity.copy(playlistsIDs = updatedPlaylists)
        tracksInPlaylistsDao.updateTrack(updatedEntity)
    }

    override suspend fun getPlaylistByID(id: Int): PlaylistEntityPresentation? {
        return playlistDao.getPlaylistById(id)?.toDomain()
    }

    override fun getAllPlaylists(): Flow<List<PlaylistEntityPresentation>> {
        return playlistDao.getAllPlaylists().map { playlists ->
            playlists.map { it.toDomain() }
        }
    }

    override suspend fun deletePlaylist(playlistID: Int) {
        val playlist = getPlaylistByID(playlistID) ?: return
        val trackIDs = playlist.tracksIDsList
            .split(",")
            .mapNotNull { it.toIntOrNull() }

        for (trackID in trackIDs) {
            removePlaylistIdFromTrack(trackID, playlistID)
            if (isPlaylistsIDsEmpty(trackID)) {
                cleanupTrackIfNotInAnyPlaylist(trackID)
            }
        }

        playlistDao.deletePlaylist(playlistID)
    }

    private fun PlaylistEntity.toDomain(): PlaylistEntityPresentation {
        return PlaylistEntityPresentation(id, name, description, image, tracksIDsList, tracksCount)
    }

    private suspend fun PlayerTrack.toData(playlistID: Int): TrackInPlaylistEntity {
        val existingPlaylists = tracksInPlaylistsDao.getPlaylistsIDsByTrackId(trackId) ?: ""
        val playlistSet = existingPlaylists.split(",").map { it.trim() }.toMutableSet()
        playlistSet += playlistID.toString()
        val newPlaylistIDsList = playlistSet.joinToString(",")
        return TrackInPlaylistEntity(
            trackId,
            trackName,
            collectionName,
            releaseDate,
            primaryGenreName,
            country,
            artistName,
            trackTime,
            previewUrl,
            artworkUrl500,
            newPlaylistIDsList,
            isLiked,
            order
        )
    }

    override fun getPlaylistTracks(): Flow<List<TrackInPlaylistEntity>> {
        return tracksInPlaylistsDao.getAllTracks()
    }


}