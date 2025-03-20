package com.practicum.playlistmaker_ver2.playlist.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker_ver2.player.ui.models.MessageState
import com.practicum.playlistmaker_ver2.playlist_editor.domain.interactor.PlaylistInteractor
import com.practicum.playlistmaker_ver2.playlist_editor.domain.models.PlaylistEntityPresentation
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {
    private val _playlistData = MutableLiveData<PlaylistPresentationState>()
    val playlistData: LiveData<PlaylistPresentationState> get() = _playlistData

    private var bottomSheetState = BottomSheetBehavior.STATE_HALF_EXPANDED

    fun restoreBottomSheetState() {
        updatePlaylistData(bottomSheet = bottomSheetState)
    }


    fun getPlaylistByID(id: Int) {
        viewModelScope.launch {
            val selectedPlaylist = playlistInteractor.getPlaylistByID(id)
            val playlistDuration = playlistDuration(id)

            if (selectedPlaylist != null) {
                _playlistData.value = _playlistData.value?.copy(
                    playlistEntity = selectedPlaylist.copy(
                        tracksDuration = playlistDuration
                    )
                )
            } else {
                updatePlaylistData(messageState = MessageState.GETTING_PLAYLIST_DATA_FAIL)
            }
        }
    }

    private suspend fun playlistDuration(id: Int): String {
        return playlistInteractor.getPlaylistTracks()
            .map { tracks ->
                val tracksDurationList = tracks.filter { id.toString() in it.playlistsIDs }
                    .map { it.trackTime }
                Log.d("DEBUG", "playlistDuration: ${tracksDurationList}")
                sumTimeStrings(tracksDurationList)
            }
            .first()
    }

    private fun sumTimeStrings(times: List<String>): String {
        var totalSeconds = 0

        for (time in times) {
            val (minutes, seconds) = time.split(":").map { it.toInt() }
            totalSeconds += minutes * 60 + seconds
        }

        val totalMinutes = (totalSeconds + 30) / 60
        return totalMinutes.toString().padStart(2, '0') + ":00"
    }

    private fun updatePlaylistData(
        bottomSheet: Int = _playlistData.value?.bottomSheet
            ?: BottomSheetBehavior.STATE_HALF_EXPANDED,
        overlayVisibility: Boolean = _playlistData.value?.overlayVisibility ?: false,
        messageState: MessageState = MessageState.NOTHING,
        playlistEntity: PlaylistEntityPresentation = _playlistData.value?.playlistEntity
            ?: PlaylistEntityPresentation()
    ) {
        _playlistData.postValue(
            PlaylistPresentationState(playlistEntity, overlayVisibility, bottomSheet, messageState)
        )
    }
}