package com.practicum.playlistmaker_ver2.playlist.domain

import com.practicum.playlistmaker_ver2.playlist.data.database.entity.PlaylistEntity
import com.practicum.playlistmaker_ver2.playlist.data.database.entity.PlaylistTrackEntity
import kotlinx.coroutines.flow.Flow

interface PlaylistDatabaseRepository {

    suspend fun insetPlayList(playList: List<PlaylistEntity>)
    suspend fun insertTrackPlayList(track: PlaylistTrackEntity)
    suspend fun getPlayList(): Flow<List<PlaylistEntity>>
    suspend fun getList():List<PlaylistEntity>
    suspend fun getPlayListId(id:Int): Flow<PlaylistEntity>
    suspend fun deletePlayList(playList: PlaylistEntity)
    suspend fun getTrackPlayList(): Flow<List<PlaylistTrackEntity>>
    suspend fun getTrackDp():List<PlaylistTrackEntity>
    suspend fun getTrack(id:Long):PlaylistTrackEntity
    suspend fun deleteTrackDb(track: PlaylistTrackEntity)
}