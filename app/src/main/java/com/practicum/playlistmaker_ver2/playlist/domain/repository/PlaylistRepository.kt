package com.practicum.playlistmaker_ver2.playlist.domain.repository

import com.practicum.playlistmaker_ver2.database.data.TrackEntity
import com.practicum.playlistmaker_ver2.playlist.data.database.entity.PlaylistEntity
import com.practicum.playlistmaker_ver2.playlist.domain.database.models.Playlist

interface PlaylistRepository {
    fun savePlayList(textNamePlayList:String,textDescription:String,filePath:String): Playlist

    suspend fun addTrackToPlayList(track : TrackEntity)
    suspend fun  getTrackId(track: String):TrackEntity
    suspend fun  deleteTrackId(playListIdTrack: List<String>,trackId:String, playList: PlaylistEntity)
    suspend fun updatePlayList(playlist:PlaylistEntity,name:String,description:String,image:String)
}