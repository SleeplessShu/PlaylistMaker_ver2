package com.practicum.playlistmaker_ver2.playlist.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker_ver2.playlist.data.database.entity.PlaylistEntity
import com.practicum.playlistmaker_ver2.playlist.data.database.entity.PlaylistTrackEntity
import com.practicum.playlistmaker_ver2.playlist.domain.interactors.PlaylistDatabaseInteractor
import com.practicum.playlistmaker_ver2.playlist.domain.interactors.PlaylistInteractor
import com.practicum.playlistmaker_ver2.playlist.presentation.models.ListPlaylistState
import com.practicum.playlistmaker_ver2.playlist.presentation.models.PlaylistIdState
import com.practicum.playlistmaker_ver2.playlist.presentation.models.PlaylistTrackGetState
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class PlaylistInfoViewModel(
    val dbInteractor: PlaylistDatabaseInteractor, private val interactor: PlaylistInteractor
) : ViewModel() {


    private val playListState = MutableLiveData<PlaylistIdState>()
    lateinit var playListSet: PlaylistEntity
    lateinit var trackListSet: List<PlaylistTrackEntity>


    fun getPlaylistState(): LiveData<PlaylistIdState> = playListState

    private val trackPlaylistState = MutableLiveData<PlaylistTrackGetState>()
    fun getTrackPlaylistState(): LiveData<PlaylistTrackGetState> = trackPlaylistState

    private val listPlayList = MutableLiveData<ListPlaylistState>()
    fun get(): LiveData<ListPlaylistState> = listPlayList

    fun getListPlaylist() {
        viewModelScope.launch {
            dbInteractor.getPlayList().collect { playlist ->
                resultList(playlist)
            }
        }
    }

    private fun resultList(playList: List<PlaylistEntity>) {
        if (playList.isEmpty()) {
            renderList(ListPlaylistState.Error("нет данных"))
        } else renderList(ListPlaylistState.Content(playList))
    }

    private fun renderList(state: ListPlaylistState) {
        listPlayList.postValue(state)
    }


    fun getPlayList(id: Int) {
        viewModelScope.launch {
            dbInteractor.getPlayListId(id).collect { playList ->
                processResult(playList)
            }
        }
    }


    private fun processResult(playList: PlaylistEntity) {
        if (playList.equals("")) {
            renderState(PlaylistIdState.Error("нет данных"))
        } else renderState(PlaylistIdState.Content(playList))
    }

    private fun renderState(state: PlaylistIdState) {
        playListState.postValue(state)
    }

    fun getTrackPlayList() {
        viewModelScope.launch {
            dbInteractor.getPlayListTrackId().collect { track ->
                result(track)
            }
        }
    }

    private fun result(track: List<PlaylistTrackEntity>) {
        if (track.isEmpty()) {
            render(PlaylistTrackGetState.Error("нет данных"))
        } else render(PlaylistTrackGetState.Content(track))

    }

    private fun render(state: PlaylistTrackGetState) {
        trackPlaylistState.postValue(state)
    }

    fun checkTrack(id: List<String>, track: List<PlaylistTrackEntity>): List<PlaylistTrackEntity> {
        val list = mutableListOf<PlaylistTrackEntity>()

        for (i in track) for (j in id) if (i.trackId.toString() == j) list.add(i)

        return list
    }

    fun setPlayList(playList: PlaylistEntity) {
        playListSet = playList
    }

    fun setTrackList(track: List<PlaylistTrackEntity>) {
        trackListSet = track
    }

    fun sumDuration(track: List<PlaylistTrackEntity>): Int {
        var duration: Int = 0
        for (i in track) {
            duration += i.trackTimeMillis
        }
        return duration
    }

    suspend fun calcPlaylistsWithTrackCount(trackId: String): Int {
        val playlists = dbInteractor.getPlayList().firstOrNull()
        if (playlists != null) {
            return playlists.count { it.trackId.contains(trackId) }
        } else {
            Log.d("DEBUG", "calcPlaylistsWithTrackCount: тонкое место выдало 0 и нужна обработка")
            return 0
        }
    }

    fun delete(deleteId: String, playlist: List<String>, id: Int) {
        viewModelScope.launch {
            val needDeleteTrack = calcPlaylistsWithTrackCount(deleteId) <= 1
            if (needDeleteTrack) {
                val track = dbInteractor.getTrack(deleteId.toLong())
                dbInteractor.deleteTrackDb(track)
                interactor.deletePlayListTrack(playlist, deleteId, playListSet)
            } else {
                interactor.deletePlayListTrack(playlist, deleteId, playListSet)
            }
            dbInteractor.getPlayListId(id).collect { playlist ->
                processResult(playlist)
            }
            dbInteractor.getPlayListTrackId().collect { track ->
                result(track)
            }
        }
    }

    fun updatePlayList(playList: PlaylistEntity, name: String, description: String, image: String) {
        viewModelScope.launch {
            interactor.updatePlayList(playList, name, description, image)
        }
    }

    fun deleteAllTrackPlayList(playList: List<String>) {

        viewModelScope.launch {
            for (i in playList) {
                val needDelete = calcPlaylistsWithTrackCount(i) <= 1
                Log.d("Sprint 23", "$needDelete")
                if (needDelete) {
                    val track = dbInteractor.getTrack(i.toLong())
                    Log.d("Sprint 23", "$track")
                    dbInteractor.deleteTrackDb(track)
                    Log.d("Sprint 23", "трек удален")
                }
            }
        }
        viewModelScope.launch {
            dbInteractor.getPlayListTrackId().collect { track ->
                result(track)
            }
        }
    }

    fun deletePlaylist(playList: PlaylistEntity) {
        viewModelScope.launch {
            dbInteractor.deletePlayList(playList)
        }
    }

    fun sharePlayList(id: Int): PlaylistEntity {
        viewModelScope.launch {
            playListSet = getPlaylistId(id)
        }
        return playListSet
    }

    suspend fun getPlaylistId(id: Int): PlaylistEntity {
        return dbInteractor.getPlayListId(id).firstOrNull() ?: PlaylistEntity(
            playListId = -1,
            namePlayList = "",
            image = "",
            description = "",
            filePath = "",
            count = -1,
            trackId = "",
        )
    }

    fun shareTrackList(id: List<String>): List<PlaylistTrackEntity> {
        viewModelScope.launch {
            trackListSet = getTrackList()
        }
        return checkTrack(id, trackListSet)
    }

    suspend fun getTrackList(): List<PlaylistTrackEntity> {
        return dbInteractor.getPlayListTrackId().firstOrNull() ?: listOf(
            PlaylistTrackEntity(
                trackId = -1L,
                trackName = "Unknown Track",
                country = "Unknown",
                releaseDate = "0000-00-00",
                collectionName = "Unknown Collection",
                primaryGenreName = "Unknown Genre",
                artistName = "Unknown Artist",
                trackTimeMillis = 0,
                artworkUrl100 = "",
                previewUrl = "",
                inFavorite = false,
                timeAdd = 0L
            )
        )
    }
}