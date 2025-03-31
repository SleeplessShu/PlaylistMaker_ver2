package com.practicum.playlistmaker_ver2.playlist_editor.presentation

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker_ver2.mediateka.domain.api.ImageInteractor
import com.practicum.playlistmaker_ver2.playlist_editor.data.entities.PlaylistEditorMessageState
import com.practicum.playlistmaker_ver2.playlist_editor.domain.interactor.PlaylistInteractor
import com.practicum.playlistmaker_ver2.playlist_editor.domain.models.PlaylistEntityPresentation
import com.practicum.playlistmaker_ver2.utils.Constants
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class PlaylistEditorViewModel(
    private val imageInteractor: ImageInteractor, private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val _playlistImage = MutableLiveData(Constants.DEFAULT_PLAYLIST_IMAGE_URI)
    val playlistImage: LiveData<Uri> get() = _playlistImage

    private val _isPlaylistValid = MutableLiveData<Boolean>()
    val isPlaylistValid: LiveData<Boolean> get() = _isPlaylistValid

    private val _isDescriptionValid = MutableLiveData<Boolean>()
    val isDescriptionValid: LiveData<Boolean> get() = _isDescriptionValid

    val toastMessage = MutableSharedFlow<PlaylistEditorMessageState>(extraBufferCapacity = 1)

    private var playlistToEdit: PlaylistEntityPresentation? = null

    var editedPlaylist: PlaylistEntityPresentation? = null
        private set

    fun initEditMode(playlist: PlaylistEntityPresentation) {
        editedPlaylist = playlist
        _playlistImage.value = Uri.parse(playlist.image)
    }

    fun isEditMode(): Boolean {
        return editedPlaylist != null
    }

    fun setPlaylistToEdit(playlist: PlaylistEntityPresentation?) {
        playlistToEdit = playlist
        playlist?.let {
            _playlistImage.postValue(Uri.parse(it.image))
            validatePlaylist(it.name)
            validateDescription(it.description)
        }
    }


    fun accessToStorageGranted() {
        viewModelScope.launch {
            toastMessage.emit(PlaylistEditorMessageState.ACCESS_GRANTED)
        }
    }

    fun onPermissionDenied(isPermissionsDenied: Boolean) {
        viewModelScope.launch {
            if (isPermissionsDenied) {
                toastMessage.emit(PlaylistEditorMessageState.CHECK_SETTINGS_FOR_ACCESS)
            } else {
                toastMessage.emit(PlaylistEditorMessageState.PERMISSION_DECLINED)
            }
        }
    }


    fun saveImageToPrivateStorage(uri: Uri?) {
        if (uri == null) {
            viewModelScope.launch {
                toastMessage.emit(PlaylistEditorMessageState.PLAYLIST_IMAGE_NOT_SELECTED)
            }
        } else {
            val savedUri = imageInteractor.saveImageToPrivateStorage(uri)
            _playlistImage.postValue(savedUri ?: _playlistImage.value)
        }
    }

    fun validatePlaylist(name: String) {
        _isPlaylistValid.postValue(name.isNotBlank())
    }

    fun validateDescription(description: String) {
        _isDescriptionValid.postValue(description.isNotBlank())
    }

    fun addOrUpdatePlaylist(title: String, description: String, onComplete: () -> Unit) {
        val imageUri =
            if (_playlistImage.value == Constants.DEFAULT_PLAYLIST_IMAGE_URI) Constants.PLACEHOLDER_URI else _playlistImage.value

        viewModelScope.launch {
            imageUri?.let { uri ->
                playlistToEdit?.let {
                    playlistInteractor.updatePlaylist(
                        playlistID = it.id,
                        image = uri,
                        title = title,
                        description = description
                    )
                } ?: playlistInteractor.addPlaylist(uri, title, description)
            }
            if (isEditMode()) {
                toastMessage.emit(PlaylistEditorMessageState.PLAYLIST_EDITS_SAVED)
            } else {
                toastMessage.emit(PlaylistEditorMessageState.PLAYLIST_CREATED)
            }
            onComplete()
        }
    }

    fun shouldShowExitDialog(): Boolean {
        return _playlistImage.value != Constants.DEFAULT_PLAYLIST_IMAGE_URI || _isPlaylistValid.value == true || _isDescriptionValid.value == true
    }

}
