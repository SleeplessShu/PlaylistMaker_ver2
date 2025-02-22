package com.practicum.playlistmaker_ver2.playlist.domain.repositories

import android.net.Uri
import com.practicum.playlistmaker_ver2.playlist.data.entities.PlaylistEntity
import com.practicum.playlistmaker_ver2.playlist.domain.models.PlaylistPresentation
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun addPlaylist(image: Uri, title: String, description: String)

    suspend fun updatePlaylist(image: Uri, title: String, description: String)

    fun getAllPlaylists(): Flow<List<PlaylistPresentation>>

    suspend fun deletePlaylist(playlistId: Int)

    suspend fun addTrack(playlistId: Int, trackId: String)

    suspend fun removeTrack(playlistId: Int, trackId: String)
}