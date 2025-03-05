package com.practicum.playlistmaker_ver2.playlist.data.repositories

import android.net.Uri
import com.practicum.playlistmaker_ver2.playlist.domain.models.PlaylistEntityPresentation
import com.practicum.playlistmaker_ver2.playlist.domain.PlaylistRepository
import com.practicum.playlistmaker_ver2.playlist.data.entities.TrackInPlaylistEntity
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerTrack
import com.practicum.playlistmaker_ver2.playlist.data.entities.PlaylistEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val tracksInPlaylistsDao: TracksInPlaylistsDao
) : PlaylistRepository {
    override suspend fun updatePlaylistAndTrack(playlistID: Int, track: PlayerTrack): Result<Unit> {
        return try {
            addTrack(playlistID, track.trackId.toString())
            addTrackInTracksInPlaylists(track)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addTrackInTracksInPlaylists(track: PlayerTrack) {
        tracksInPlaylistsDao.addTrackToTracksInPlaylists(track.toData())
    }

    override suspend fun addPlaylist(image: Uri, title: String, description: String) {
        val entity = PlaylistEntity(
            name = title,
            description = description,
            image = image.toString(),
        )
        playlistDao.addPlaylist(entity)
    }

    override suspend fun updatePlaylist(image: Uri, title: String, description: String) {
        val playlist = playlistDao.getAllPlaylists()
            .map { playlists ->
                playlists.find { it.name == title }
            }.firstOrNull()

        playlist?.let {
            val updatedPlaylist = it.copy(
                image = image.toString(),
                description = description
            )
            playlistDao.updatePlaylist(updatedPlaylist)
        }
    }

    override suspend fun addTrack(playlistId: Int, trackId: String) {
        playlistDao.addTrackToPlaylist(playlistId, trackId)
    }

    override suspend fun removeTrack(playlistId: Int, trackId: String) {
        playlistDao.removeTrackFromPlaylist(playlistId, trackId)
    }

    override fun getAllPlaylists(): Flow<List<PlaylistEntityPresentation>> {
        return playlistDao.getAllPlaylists().map { playlists ->
            playlists.map { it.toDomain() }
        }
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        playlistDao.deletePlaylist(playlistId)
    }

    private fun PlaylistEntity.toDomain(): PlaylistEntityPresentation {
        return PlaylistEntityPresentation(id, name, description, image, tracksIDsList, tracksCount)
    }

    private fun PlayerTrack.toData(): TrackInPlaylistEntity {
        return TrackInPlaylistEntity(trackId, trackName, collectionName, releaseDate, primaryGenreName, country, artistName, trackTime, previewUrl, artworkUrl500, isLiked, order)
    }
}