package com.practicum.playlistmaker_ver2.playlist.domain.interactorsImpl

import com.practicum.playlistmaker_ver2.database.data.TrackEntity
import com.practicum.playlistmaker_ver2.playlist.data.database.entity.PlaylistEntity
import com.practicum.playlistmaker_ver2.playlist.domain.database.models.Playlist
import com.practicum.playlistmaker_ver2.playlist.domain.interactors.PlaylistInteractor
import com.practicum.playlistmaker_ver2.playlist.domain.repository.PlaylistRepository

class PlaylistInteractorImpl (private val repository: PlaylistRepository): PlaylistInteractor {
    override fun savePlayList(textNamePlayList:String,textDescription:String,filePath:String): Playlist {
        return repository.savePlayList(textNamePlayList,textDescription,filePath)
    }

    override suspend fun addTrackToPlayList(track: TrackEntity) {
        return repository.addTrackToPlayList(track)
    }

    override suspend fun getTrackId(track: String):TrackEntity {
        return repository.getTrackId(track)
    }

    override suspend fun deletePlayListTrack(
        playListId: List<String>,
        trackId: String,
        playListEntity: PlaylistEntity
    ) {
        return repository.deleteTrackId(playListId,trackId,playListEntity)
    }

    override suspend fun updatePlayList(
        playlist: PlaylistEntity,
        name: String,
        description: String,
        image: String
    ) {
        return repository.updatePlayList(playlist,name,description,image)
    }

}