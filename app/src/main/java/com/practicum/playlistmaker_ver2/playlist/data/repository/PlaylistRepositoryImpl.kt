package com.practicum.playlistmaker_ver2.playlist.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker_ver2.AppDataBase
import com.practicum.playlistmaker_ver2.database.data.TrackEntity
import com.practicum.playlistmaker_ver2.playlist.data.database.entity.PlaylistEntity
import com.practicum.playlistmaker_ver2.playlist.domain.database.models.Playlist
import com.practicum.playlistmaker_ver2.playlist.domain.repository.PlaylistRepository
import com.practicum.playlistmaker_ver2.utils.toTrackPlaylistEntity

class PlaylistRepositoryImpl (private val appDataBase: AppDataBase): PlaylistRepository {
    override fun savePlayList(
        textNamePlayList: String,
        textDescription: String,
        filePath: String
    ): Playlist {
        val list = Playlist(
            playListId = 0,
            namePlayList = textNamePlayList,
            description = textDescription,
            image = filePath,
            filePath = filePath,
            count = 0,
            trackId = arrayListOf(),
        )
        return list
    }

    override suspend fun addTrackToPlayList(track: TrackEntity) {
        appDataBase.playListTrackDao().insertPlayListTrack(track.toTrackPlaylistEntity())
    }

    override suspend fun getTrackId(track: String): TrackEntity {
        return appDataBase.trackDao().getTrackById(track.toInt())
    }

    override suspend fun deleteTrackId(
        playListId: List<String>,
        trackId: String,
        playList: PlaylistEntity
    ) {
        val list = createTracksFromJson(playList.trackId)
        list.remove(trackId)

        val current = list.filter { track -> track == trackId }
        if (current.isEmpty()) {
            appDataBase.playListDao().updatePlayList(
                playList.copy(
                    trackId = createJsonFromTracks(
                        list
                    ),
                    count = list.size
                )
            )
        }
    }

    override suspend fun updatePlayList(playlist:PlaylistEntity,name:String,description:String,image:String) {
        appDataBase.playListDao().updatePlayList(
            playlist.copy(
                namePlayList = name,
                description = description,
                image = image,
                filePath = image))
    }

    fun createJsonFromTracks(tracks: List<String>): String {
        return Gson().toJson(tracks)
    }

    fun createTracksFromJson(json: String): ArrayList<String> {
        if (json == "") return ArrayList()
        val trackListType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(json, trackListType)
    }
}

