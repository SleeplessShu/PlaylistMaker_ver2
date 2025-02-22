package com.practicum.playlistmaker_ver2.playlist.data.repository

import android.net.Uri
import com.practicum.playlistmaker_ver2.playlist.data.entities.PlaylistEntity
import com.practicum.playlistmaker_ver2.playlist.database.PlaylistDao
import com.practicum.playlistmaker_ver2.playlist.domain.models.PlaylistPresentation
import com.practicum.playlistmaker_ver2.playlist.domain.repositories.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao
) : PlaylistRepository {
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

    override fun getAllPlaylists(): Flow<List<PlaylistPresentation>> {
        return playlistDao.getAllPlaylists().map { playlists ->
            playlists.map { it.toDomain() }
        }
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        playlistDao.deletePlaylist(playlistId)
    }

    private fun PlaylistEntity.toDomain(): PlaylistPresentation {
        return PlaylistPresentation(id, name, description, image, tracksIDsList, tracksCount)
    }
}