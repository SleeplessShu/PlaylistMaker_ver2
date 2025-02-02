package com.practicum.playlistmaker_ver2.playlist.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker_ver2.AppDataBase
import com.practicum.playlistmaker_ver2.playlist.data.database.entity.PlaylistEntity
import com.practicum.playlistmaker_ver2.playlist.domain.database.models.Playlist
import com.practicum.playlistmaker_ver2.playlist.domain.interactors.PlaylistDatabaseInteractor
import com.practicum.playlistmaker_ver2.playlist.domain.interactors.PlaylistInteractor
import com.practicum.playlistmaker_ver2.playlist.presentation.models.PlaylistState
import kotlinx.coroutines.launch

class CreatePlaylistViewModel (
    val interactor: PlaylistInteractor,
    val appDataBase: AppDataBase,
    private val dbInteractor: PlaylistDatabaseInteractor
) : ViewModel() {


    private var namePlaylist: String = ""
    private var descriptionPlayList: String = ""
    private var filePath: String = ""
    private val playList = ArrayList<Playlist>()
    private val playListEntity = ArrayList<PlaylistEntity>()

    private val bottomState = MutableLiveData<Boolean>()
    fun getBottomState(): LiveData<Boolean> = bottomState

    private val playListState = MutableLiveData<PlaylistState>()
    fun getPlayListState(): LiveData<PlaylistState> = playListState
    fun checkBottom(textName: String) {
        if (textName.isNotBlank()) {
            viewModelScope.launch {
                bottomState.postValue(true)
            }
        } else {
            viewModelScope.launch {
                bottomState.postValue(false)
            }
        }
    }

    fun saveName(name: String) {
        this.namePlaylist = name
    }

    fun saveDescription(description: String) {
        this.descriptionPlayList = description
    }

    fun saveFilePath(file: String) {
        this.filePath = file
    }

    fun getPlayList() {
        viewModelScope.launch {
            dbInteractor.getPlayList().collect { playList ->
                processResult(playList)

            }
        }
    }

    private fun processResult(playList: List<PlaylistEntity>) {
        if (playList.isEmpty()) {
            renderState(PlaylistState.Error("Ваша медиатека пуста"))
        } else {
            renderState(PlaylistState.Content(playList))
        }
    }


    private fun renderState(state: PlaylistState) {
        playListState.postValue(state)
    }


    fun savePlayList() {
        val list = interactor.savePlayList(namePlaylist, descriptionPlayList, filePath)
        this.playList.add(list)
        playList.size


        this.playListEntity.add(list.toPlayListEntity())
        viewModelScope.launch { appDataBase.playListDao().insertPlayList(playListEntity) }

    }

    fun Playlist.toPlayListEntity() = PlaylistEntity(
        playListId,
        namePlayList,
        image,
        description,
        filePath,
        count,
        trackId.toString(),
    )
}
