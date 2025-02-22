package com.practicum.playlistmaker_ver2.mediateka.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker_ver2.playlist.domain.api.PlaylistInteractor
import com.practicum.playlistmaker_ver2.playlist.domain.models.PlaylistPresentation
import kotlinx.coroutines.launch

class TabPlaylistsViewModel(private val interactor: PlaylistInteractor) : ViewModel() {
    private val _playlists = MutableLiveData<List<PlaylistPresentation>>()
    val playlists: LiveData<List<PlaylistPresentation>> = _playlists

    fun loadPlaylists() {
        viewModelScope.launch {
            interactor.getAllPlaylists().collect { playlistList ->
                _playlists.postValue(playlistList)
            }
        }
    }
}