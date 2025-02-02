package com.practicum.playlistmaker_ver2.playlist.data.database.repository

import com.practicum.playlistmaker_ver2.AppDataBase
import com.practicum.playlistmaker_ver2.playlist.data.database.entity.PlaylistEntity
import com.practicum.playlistmaker_ver2.playlist.data.database.entity.PlaylistTrackEntity
import com.practicum.playlistmaker_ver2.playlist.domain.PlaylistDatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistDatabaseRepositoryImpl(private val appDataBase: AppDataBase): PlaylistDatabaseRepository {
    override suspend fun insetPlayList(playList: List<PlaylistEntity>) {
        appDataBase.playListDao().insertPlayList(playList)
    }

    override suspend fun insertTrackPlayList(track: PlaylistTrackEntity) {
        appDataBase.playListTrackDao().insertPlayListTrack(track)
    }

    override suspend fun getPlayList(): Flow<List<PlaylistEntity>> = flow {
        val playList = appDataBase.playListDao().getPlayList()
        emit(playList)
    }

    override suspend fun getList(): List<PlaylistEntity> {
        val list = appDataBase.playListDao().getPlayList()
        return  list
    }

    override suspend fun getPlayListId(id:Int): Flow<PlaylistEntity> = flow {
        val playList = appDataBase.playListDao().getPlayListId(id)
        emit(playList)
    }

    override suspend fun deletePlayList(playList: PlaylistEntity) {
        appDataBase.playListDao().deletePlayList(playList)
    }

    override suspend fun getTrackPlayList(): Flow<List<PlaylistTrackEntity>> = flow{
        val track = appDataBase.playListTrackDao().getPlayListTrackId()
        emit((track))
    }

    override suspend fun getTrackDp(): List<PlaylistTrackEntity> {
        val track = appDataBase.playListTrackDao().getPlayListTrackId()
        return track
    }

    override suspend fun getTrack(id: Long): PlaylistTrackEntity {
        val track = appDataBase.playListTrackDao().getTrackDb(id)
        return track
    }

    override suspend fun deleteTrackDb(track: PlaylistTrackEntity) {
        appDataBase.playListTrackDao().deleteTrack(track)
    }


}