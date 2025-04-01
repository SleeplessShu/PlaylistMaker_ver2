package com.practicum.playlistmaker_ver2.playlist_editor.domain.implimentation

import android.net.Uri
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerTrack
import com.practicum.playlistmaker_ver2.playlist_editor.data.entities.TrackInPlaylistEntity
import com.practicum.playlistmaker_ver2.playlist_editor.domain.PlaylistRepository
import com.practicum.playlistmaker_ver2.playlist_editor.domain.interactor.PlaylistInteractor
import com.practicum.playlistmaker_ver2.playlist_editor.domain.models.PlaylistEntityPresentation
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    val playlistRepository: PlaylistRepository
) : PlaylistInteractor {

    override suspend fun addOrUpdateTrackInPlaylist(
        playlistId: Int, track: PlayerTrack
    ): Result<Unit> {
        return playlistRepository.addOrUpdateTrackInPlaylist(playlistId = playlistId, track = track)
    }

    override suspend fun addPlaylist(image: Uri, title: String, description: String) {
        playlistRepository.addPlaylist(image, title, description)
    }

    override suspend fun updatePlaylist(
        playlistID: Int, image: Uri, title: String, description: String
    ) {
        playlistRepository.updatePlaylist(playlistID, image, title, description)
    }

    override fun getAllPlaylists(): Flow<List<PlaylistEntityPresentation>> {
        return playlistRepository.getAllPlaylists()
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        return playlistRepository.deletePlaylist(playlistId)
    }

    override suspend fun removeTrack(playlistId: Int, trackId: Int) {
        playlistRepository.removeTrack(playlistId, trackId)
    }

    override suspend fun getPlaylistByID(id: Int): PlaylistEntityPresentation? {
        return playlistRepository.getPlaylistByID(id)
    }

    override fun getPlaylistTracks(): Flow<List<TrackInPlaylistEntity>> {
        return playlistRepository.getPlaylistTracks()
    }
}