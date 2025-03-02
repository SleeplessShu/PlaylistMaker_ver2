package com.practicum.playlistmaker_ver2.database.Playlists.domain

import android.net.Uri
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerTrack
import com.practicum.playlistmaker_ver2.playlist.domain.models.PlaylistEntityPresentation
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(val playlistRepository: PlaylistRepository) : PlaylistInteractor {
    override suspend fun updatePlaylistAndTrack(playlistID: Int, track: PlayerTrack): Result<Unit> {
        return playlistRepository.updatePlaylistAndTrack(playlistID, track)
    }

    override suspend fun addPlaylist(image: Uri, title: String, description: String) {
        playlistRepository.addPlaylist(image, title, description)
    }

    override suspend fun updatePlaylist(image: Uri, title: String, description: String) {
        playlistRepository.updatePlaylist(image, title, description)
    }



    override fun getAllPlaylists(): Flow<List<PlaylistEntityPresentation>> {
        return playlistRepository.getAllPlaylists()
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        return playlistRepository.deletePlaylist(playlistId)
    }
    override suspend fun addTrack(playlistId: Int, trackId: String) {
        playlistRepository.addTrack(playlistId, trackId)
    }

    override suspend fun removeTrack(playlistId: Int, trackId: String) {
        playlistRepository.removeTrack(playlistId, trackId)
    }
}