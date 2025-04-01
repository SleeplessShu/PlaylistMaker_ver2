package com.practicum.playlistmaker_ver2.playlist.presentation

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.player.ui.models.MessageState
import com.practicum.playlistmaker_ver2.playlist.presentation.models.TracksInPlaylistState
import com.practicum.playlistmaker_ver2.playlist_editor.data.entities.TrackInPlaylistEntity
import com.practicum.playlistmaker_ver2.playlist_editor.data.entities.toTrack
import com.practicum.playlistmaker_ver2.playlist_editor.domain.interactor.PlaylistInteractor
import com.practicum.playlistmaker_ver2.playlist_editor.domain.models.PlaylistEntityPresentation
import com.practicum.playlistmaker_ver2.settings.domain.api.SharingInteractor
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val sharingInteractor: SharingInteractor,
    private val resources: Resources
) : ViewModel() {

    val toastMessage = MutableSharedFlow<MessageState>(extraBufferCapacity = 1)

    private val _playlistData = MutableLiveData<PlaylistPresentationState>()
    val playlistData: LiveData<PlaylistPresentationState> get() = _playlistData

    private val _currentPlaylistID = MutableStateFlow<Int?>(null)
    private val _playlistTrackState =
        MutableStateFlow<TracksInPlaylistState>(TracksInPlaylistState.Empty(message = MessageState.NOTHING))
    val tracksData: StateFlow<TracksInPlaylistState> get() = _playlistTrackState

    fun shareButtonPressed() {
        if (_playlistData.value?.playlistEntity?.tracksCount == 0) {
            viewModelScope.launch {
                toastMessage.emit(MessageState.NOTHING_TO_SHARE)
            }
        } else {
            sharePlaylist()
        }
    }

    fun optionsButtonPressed() {
        renderPlaylistState(
            bottomSheetOptions = BottomSheetBehavior.STATE_COLLAPSED,
            overlayVisibility = true
        )
    }

    fun bottomSheetOptionsCollapsed() {
        renderPlaylistState(
            bottomSheetOptions = BottomSheetBehavior.STATE_HIDDEN,
            overlayVisibility = false
        )
    }

    fun restoreBottomSheetOptionsState() {
        val state = _playlistData.value ?: return
        renderPlaylistState(
            bottomSheetOptions = state.bottomSheetOptions,
            overlayVisibility = state.overlayVisibility
        )
    }


    fun getPlaylistByID(playlistID: Int) {
        viewModelScope.launch {
            val selectedPlaylist = playlistInteractor.getPlaylistByID(playlistID)
            if (selectedPlaylist == null) {
                renderPlaylistState(messageState = MessageState.GETTING_PLAYLIST_DATA_FAIL)
                return@launch
            }

            val allTracks = playlistInteractor.getPlaylistTracks().first()
            val trackMap = allTracks
                .filter { playlistID.toString() in it.playlistsIDs }
                .associateBy { it.trackId.toString() }

            val orderedTracks = selectedPlaylist.tracksIDsList
                .split(",")
                .asReversed()
                .mapNotNull { trackMap[it] }
                .map { it.toTrack() }

            val state = if (orderedTracks.isEmpty()) {
                TracksInPlaylistState.Empty(message = MessageState.NOTHING)
            } else {
                TracksInPlaylistState.Content(orderedTracks, MessageState.NOTHING)
            }

            _playlistTrackState.value = state

            val duration = sumTimeStrings(orderedTracks.map { it.trackTime })

            _playlistData.value = PlaylistPresentationState(
                playlistEntity = selectedPlaylist.copy(tracksDuration = duration),
                overlayVisibility = false,
                bottomSheetTracks = BottomSheetBehavior.STATE_COLLAPSED,
                bottomSheetOptions = BottomSheetBehavior.STATE_HIDDEN,
                messageState = MessageState.NOTHING
            )
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

    fun removePlaylistFromDB() {
        val playlistID: Int = _playlistData.value?.playlistEntity?.id ?: return

        viewModelScope.launch {
            playlistInteractor.deletePlaylist(playlistID)
        }
    }

    private suspend fun playlistDuration(playlistID: Int): String {
        return playlistInteractor.getPlaylistTracks().map { tracks ->
            val allTracksTime = filterTracksByPlaylist(tracks, playlistID).map { it.trackTime }
            sumTimeStrings(allTracksTime)
        }.first()
    }

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
        bottomSheetTracks: Int = _playlistData.value?.bottomSheetTracks
            ?: BottomSheetBehavior.STATE_HALF_EXPANDED,
        bottomSheetOptions: Int = _playlistData.value?.bottomSheetOptions
            ?: BottomSheetBehavior.STATE_HIDDEN,
        overlayVisibility: Boolean = _playlistData.value?.overlayVisibility ?: false,
        messageState: MessageState = MessageState.NOTHING,
        playlistEntity: PlaylistEntityPresentation = _playlistData.value?.playlistEntity
            ?: PlaylistEntityPresentation()
    ) {
        _playlistData.postValue(
            PlaylistPresentationState(
                playlistEntity,
                overlayVisibility,
                bottomSheetTracks,
                bottomSheetOptions,
                messageState
            )
        )
    }

    private fun filterTracksByPlaylist(
        tracks: List<TrackInPlaylistEntity>, playlistID: Int
    ): List<TrackInPlaylistEntity> {
        return tracks.filter { playlistID.toString() in it.playlistsIDs }
    }

    private fun sharePlaylist() {
        val playlist = _playlistData.value?.playlistEntity ?: return
        val tracksCount = playlist.tracksCount
        val tracksCountText = resources.getQuantityString(
            R.plurals.track_count, tracksCount, tracksCount
        )
        val tracksDataAsText = tracksToText()
        val playlistTextData = buildString {
            appendLine(playlist.name)
            appendLine(playlist.description)
            appendLine(tracksCountText)
            appendLine(tracksDataAsText)
        }
        sharingInteractor.sharePlaylist(playlistTextData)
    }

    private fun tracksToText(): String {
        val currentTracks =
            (_playlistTrackState.value as? TracksInPlaylistState.Content)?.tracks ?: return ""
        return currentTracks.withIndex().joinToString("\n") { (i, track) ->
            "${i + 1}. ${track.artistName} - ${track.trackName} (${track.trackTime})"
        }
    }


}

