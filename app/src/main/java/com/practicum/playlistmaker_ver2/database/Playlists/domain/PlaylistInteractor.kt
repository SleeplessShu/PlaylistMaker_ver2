package com.practicum.playlistmaker_ver2.database.Playlists.domain

import android.net.Uri
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerTrack
import com.practicum.playlistmaker_ver2.playlist.domain.models.PlaylistEntityPresentation
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun updatePlaylistAndTrack(playlistID: Int, track: PlayerTrack) : Result<Unit>

    suspend fun addPlaylist(image: Uri, title: String, description: String)

    suspend fun updatePlaylist(image: Uri, title: String, description: String)

    fun getAllPlaylists(): Flow<List<PlaylistEntityPresentation>>

    suspend fun deletePlaylist(playlistId: Int)

    suspend fun addTrack(playlistId: Int, trackId: String)

    suspend fun removeTrack(playlistId: Int, trackId: String)
}