package com.practicum.playlistmaker_ver2.playlist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker_ver2.player.ui.models.MessageState
import com.practicum.playlistmaker_ver2.playlist.presentation.models.TracksInPlaylistState
import com.practicum.playlistmaker_ver2.playlist_editor.data.entities.TrackInPlaylistEntity
import com.practicum.playlistmaker_ver2.playlist_editor.data.entities.toTrack
import com.practicum.playlistmaker_ver2.playlist_editor.domain.interactor.PlaylistInteractor
import com.practicum.playlistmaker_ver2.playlist_editor.domain.models.PlaylistEntityPresentation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {
    private val _playlistData = MutableLiveData<PlaylistPresentationState>()
    val playlistData: LiveData<PlaylistPresentationState> get() = _playlistData/*
         private val _tracksData = MutableLiveData<TracksInPlaylistState>()
         val tracksData: LiveData<TracksInPlaylistState> get() = _tracksData*/

    private var bottomSheetState = BottomSheetBehavior.STATE_HALF_EXPANDED

    private val _currentPlaylistID = MutableStateFlow<Int?>(null)
    private val _playlistTrackState =
        MutableStateFlow<TracksInPlaylistState>(TracksInPlaylistState.Empty(""))
    val tracksData: StateFlow<TracksInPlaylistState> get() = _playlistTrackState

    init {
        viewModelScope.launch {
            _currentPlaylistID.filterNotNull().collect { playlistID ->
                playlistInteractor.getPlaylistTracks().map { allTracks ->
                    val filtered = allTracks.filter { playlistID.toString() in it.playlistsIDs }
                        .map { it.toTrack() }

                    if (filtered.isEmpty()) {
                        TracksInPlaylistState.Empty("")
                    } else {
                        TracksInPlaylistState.Content(filtered)
                    }
                }.collect { state ->
                    _playlistTrackState.value = state
                }
            }
        }
    }


    fun restoreBottomSheetState() {
        renderPlaylistState(bottomSheet = bottomSheetState)
    }


    fun getPlaylistByID(playlistID: Int) {
        viewModelScope.launch {
            /*val selectedPlaylist = playlistInteractor.getPlaylistByID(playlistID)
            val playlistDuration = playlistDuration(playlistID)

            if (selectedPlaylist != null) {
                _playlistData.value = _playlistData.value?.copy(
                    playlistEntity = selectedPlaylist.copy(
                        tracksDuration = playlistDuration
                    )
                )
            } else {
                renderPlaylistState(messageState = MessageState.GETTING_PLAYLIST_DATA_FAIL)
            }*/
            _currentPlaylistID.value = playlistID

            val selectedPlaylist = playlistInteractor.getPlaylistByID(playlistID)
            val duration = playlistDuration(playlistID)

            if (selectedPlaylist != null) {
                _playlistData.value = PlaylistPresentationState(
                    playlistEntity = selectedPlaylist.copy(tracksDuration = duration),
                    overlayVisibility = false,
                    bottomSheet = BottomSheetBehavior.STATE_HALF_EXPANDED,
                    messageState = MessageState.NOTHING
                )
            } else {
                renderPlaylistState(messageState = MessageState.GETTING_PLAYLIST_DATA_FAIL)
            }
        }
    }

    fun removeTrackFromPlaylist(trackID: Int) {
        val playlistID: Int = _playlistData.value?.playlistEntity?.id ?: return
        viewModelScope.launch {
            playlistInteractor.removeTrack(playlistID, trackID)
            getPlaylistByID(playlistID)
        }
        getPlaylistByID(playlistID)
    }

    private suspend fun playlistDuration(playlistID: Int): String {
        return playlistInteractor.getPlaylistTracks().map { tracks ->
            //mapToPresentation(tracks)
            val allTracksTime = filterTracksByPlaylist(tracks, playlistID).map { it.trackTime }
            sumTimeStrings(allTracksTime)
        }.first()
    }

    /*private fun mapToPresentation(tracksList: List<TrackInPlaylistEntity>) {
        val tracks = tracksList.map { it.toTrack() }
        processResult(tracks)

    }*/

    private fun sumTimeStrings(times: List<String>): String {
        var totalSeconds = 0

        for (time in times) {
            val (minutes, seconds) = time.split(":").map { it.toInt() }
            totalSeconds += minutes * 60 + seconds
        }

        val totalMinutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return totalMinutes.toString().padStart(2, '0') + ":" + seconds.toString().padStart(2, '0')
    }

    private fun renderPlaylistState(
        bottomSheet: Int = _playlistData.value?.bottomSheet
            ?: BottomSheetBehavior.STATE_HALF_EXPANDED,
        overlayVisibility: Boolean = _playlistData.value?.overlayVisibility ?: false,
        messageState: MessageState = MessageState.NOTHING,
        playlistEntity: PlaylistEntityPresentation = _playlistData.value?.playlistEntity
            ?: PlaylistEntityPresentation()
    ) {
        _playlistData.postValue(
            PlaylistPresentationState(
                playlistEntity, overlayVisibility, bottomSheet, messageState
            )
        )
    }

    /*private fun processResult(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            renderAdapterState((TracksInPlaylistState.Empty("")))
        } else {
            renderAdapterState(TracksInPlaylistState.Content(tracks))
        }

    }*/

    /*private fun renderAdapterState(state: TracksInPlaylistState) {
        _tracksData.postValue(state)
    }*/

    private fun filterTracksByPlaylist(
        tracks: List<TrackInPlaylistEntity>, playlistID: Int
    ): List<TrackInPlaylistEntity> {
        return tracks.filter { playlistID.toString() in it.playlistsIDs }
    }


}