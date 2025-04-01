package com.practicum.playlistmaker_ver2.playlist_editor.domain

import android.net.Uri
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerTrack
import com.practicum.playlistmaker_ver2.playlist_editor.data.entities.TrackInPlaylistEntity
import com.practicum.playlistmaker_ver2.playlist_editor.domain.models.PlaylistEntityPresentation
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun addOrUpdateTrackInPlaylist(playlistId: Int, track: PlayerTrack): Result<Unit>

    suspend fun addPlaylist(image: Uri, title: String, description: String)

    suspend fun updatePlaylist(playlistID:Int, image: Uri, title: String, description: String)

    fun getAllPlaylists(): Flow<List<PlaylistEntityPresentation>>

    suspend fun deletePlaylist(playlistID: Int)

    suspend fun removeTrack(playlistID: Int, trackID: Int)

    suspend fun getPlaylistByID(id: Int): PlaylistEntityPresentation?

    fun getPlaylistTracks(): Flow<List<TrackInPlaylistEntity>>
}