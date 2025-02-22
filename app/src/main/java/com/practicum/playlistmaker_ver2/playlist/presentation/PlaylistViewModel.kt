package com.practicum.playlistmaker_ver2.playlist.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker_ver2.mediateka.domain.api.ImageInteractor
import com.practicum.playlistmaker_ver2.playlist.domain.api.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val imageInteractor: ImageInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    fun saveImageToPrivateStorage(uri: Uri): Uri? {
        return imageInteractor.saveImageToPrivateStorage(uri)
    }

    fun addPlaylist(imageUri: Uri, title: String, description: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            playlistInteractor.addPlaylist(imageUri, title, description)
            onComplete() 
        }
    }
}