package com.practicum.playlistmaker_ver2.playlist.domain.interactors

import com.practicum.playlistmaker_ver2.playlist.data.database.entity.PlaylistEntity
import com.practicum.playlistmaker_ver2.playlist.data.database.entity.PlaylistTrackEntity
import kotlinx.coroutines.flow.Flow

interface PlaylistDatabaseInteractor {
    suspend fun insetPlaylist(playList: List<PlaylistEntity>)
    suspend fun insertTrackPlayList(track: PlaylistTrackEntity)
    suspend fun getPlayList(): Flow<List<PlaylistEntity>>
    suspend fun getPlayListId(id:Int): Flow<PlaylistEntity>
    suspend fun deletePlayList(playList: PlaylistEntity)
    suspend fun getPlayListTrackId(): Flow<List<PlaylistTrackEntity>>
    suspend fun getTrackDb():List<PlaylistTrackEntity>
    suspend fun getList():List<PlaylistEntity>
    suspend fun getTrack(id:Long):PlaylistTrackEntity
    suspend fun deleteTrackDb(track: PlaylistTrackEntity)
}