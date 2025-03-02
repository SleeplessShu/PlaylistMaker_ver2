package com.practicum.playlistmaker_ver2.playlist.presentation

import android.net.Uri

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker_ver2.mediateka.domain.api.ImageInteractor
import com.practicum.playlistmaker_ver2.database.Playlists.domain.PlaylistInteractor
import com.practicum.playlistmaker_ver2.utils.Constants
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val imageInteractor: ImageInteractor, private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val _playlistImage = MutableLiveData(Constants.DEFAULT_PLAYLIST_IMAGE_URI)
    val playlistImage: LiveData<Uri> get() = _playlistImage

    private val _isPlaylistValid = MutableLiveData<Boolean>()
    val isPlaylistValid: LiveData<Boolean> get() = _isPlaylistValid


    fun saveImageToPrivateStorage(uri: Uri) {
        val savedUri = imageInteractor.saveImageToPrivateStorage(uri)
        _playlistImage.postValue(savedUri ?: _playlistImage.value)
    }

    fun validatePlaylist(name: String) {
        _isPlaylistValid.postValue(name.isNotBlank())
    }

    fun addPlaylist(title: String, description: String, onComplete: () -> Unit) {
        val imageUri = if(_playlistImage.value == Constants.DEFAULT_PLAYLIST_IMAGE_URI) Constants.PLACEHOLDER_URI else _playlistImage.value

        viewModelScope.launch {
            if (imageUri != null) {
                playlistInteractor.addPlaylist(imageUri, title, description)
            }
            onComplete()
        }
    }

    fun shouldShowExitDialog(): Boolean {
        return _playlistImage.value != Constants.DEFAULT_PLAYLIST_IMAGE_URI || _isPlaylistValid.value == true
    }

}
