package com.practicum.playlistmaker_ver2.playlist.domain.interactorsImpl

import com.practicum.playlistmaker_ver2.playlist.data.database.entity.PlaylistEntity
import com.practicum.playlistmaker_ver2.playlist.data.database.entity.PlaylistTrackEntity
import com.practicum.playlistmaker_ver2.playlist.domain.PlaylistDatabaseRepository
import com.practicum.playlistmaker_ver2.playlist.domain.interactors.PlaylistDatabaseInteractor
import kotlinx.coroutines.flow.Flow

class PlaylistDatabaseInteractorImpl(val repository: PlaylistDatabaseRepository) :
    PlaylistDatabaseInteractor {
    override suspend fun insetPlaylist(playList: List<PlaylistEntity>) {
        return repository.insetPlayList(playList)
    }

    override suspend fun insertTrackPlayList(track: PlaylistTrackEntity) {
        return repository.insertTrackPlayList(track)
    }

    override suspend fun getPlayList(): Flow<List<PlaylistEntity>> {
        return repository.getPlayList()
    }

    override suspend fun getPlayListId(id: Int): Flow<PlaylistEntity> {
        return repository.getPlayListId(id)
    }

    override suspend fun deletePlayList(playList: PlaylistEntity) {
        return repository.deletePlayList(playList)
    }

    override suspend fun getPlayListTrackId(): Flow<List<PlaylistTrackEntity>> {
        return repository.getTrackPlayList()
    }

    override suspend fun getTrackDb(): List<PlaylistTrackEntity> {
        return repository.getTrackDp()
    }

    override suspend fun getList(): List<PlaylistEntity> {
        return repository.getList()
    }

    override suspend fun getTrack(id: Long): PlaylistTrackEntity {
        return repository.getTrack(id)
    }

    override suspend fun deleteTrackDb(track: PlaylistTrackEntity) {
        return repository.deleteTrackDb(track)
    }

}