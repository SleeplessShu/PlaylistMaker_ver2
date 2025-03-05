package com.practicum.playlistmaker_ver2.player.ui

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker_ver2.database.LikedTracks.domain.LikedTracksInteractor
import com.practicum.playlistmaker_ver2.database.Playlists.domain.PlaylistInteractor
import com.practicum.playlistmaker_ver2.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker_ver2.player.ui.models.MessageState
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerState
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerTrack
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerViewState
import com.practicum.playlistmaker_ver2.player.ui.models.UiState
import com.practicum.playlistmaker_ver2.playlist.domain.models.PlaylistEntityPresentation
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class PlayerViewModel(
    private val interactorPlayer: PlayerInteractor,
    private val interactorPlaylist: PlaylistInteractor,
    private val interactorLikedTracks: LikedTracksInteractor,
    private val mainThreadHandler: Handler,
) : ViewModel() {

    private val _viewState = MutableLiveData<PlayerViewState>()
    val viewState: LiveData<PlayerViewState> get() = _viewState

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> get() = _uiState

    private val _track = MutableLiveData<PlayerTrack>()
    val track: LiveData<PlayerTrack> get() = _track

    private val _playlists = MutableLiveData<List<PlaylistEntityPresentation>>()
    val playlists: LiveData<List<PlaylistEntityPresentation>> = _playlists

    private var savedTrackUrl = ""
    private var timerJob: Job? = null
    private var bottomSheetState = BottomSheetBehavior.STATE_HIDDEN

    fun setupPlayer(trackUrl: String) {
        if (savedTrackUrl == trackUrl && interactorPlayer.isPrepared()) {
            return
        }

        savedTrackUrl = trackUrl
        interactorPlayer.prepare(trackUrl, onPrepared = {
            updatePlayerState(PlayerState.PREPARED, 0L, null)
        },
            onCompletion = {
                mainThreadHandler.post {
                    stopPlayingTimeCounter()
                    interactorPlayer.seekTo(0)
                    updatePlayerState(PlayerState.PREPARED, 0L, null)
                }
            },
            onError = { what, extra ->
                val errorMessage = "Error occurred: $what, $extra"
                mainThreadHandler.post {
                    updatePlayerState(PlayerState.ERROR, 0L, errorMessage)
                }
                true
            })
    }

    fun playPause() {
        if (!interactorPlayer.isPrepared()) {
            setupPlayer(savedTrackUrl)
            return
        }

        if (interactorPlayer.isPlaying()) {
            pausePlayer()
        } else {
            startPlayer()
        }
    }

    fun releasePlayer() {
        stopPlayingTimeCounter()
        interactorPlayer.releasePlayer()
        updatePlayerState(PlayerState.RELEASED, 0L, null)
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        releasePlayer()
    }

    fun restoreBottomSheetState() {
        updateUiState(bottomSheet = bottomSheetState)
    }

    fun bottomSheetCollapsed() {
        bottomSheetState = BottomSheetBehavior.STATE_HIDDEN
        updateUiState(bottomSheet = BottomSheetBehavior.STATE_HIDDEN, overlayVisibility = false)

        if (!interactorPlayer.isPlaying()) {
            setupPlayer(savedTrackUrl)
        }
    }

    fun openBottomSheet() {
        loadPlaylists()
        bottomSheetState = BottomSheetBehavior.STATE_EXPANDED
        updateUiState(bottomSheet = BottomSheetBehavior.STATE_EXPANDED, overlayVisibility = true)
    }

    fun onPlaylistClick(playlist: PlaylistEntityPresentation, currentTrack: PlayerTrack) {
        if (currentTrack.trackId.toString() in playlist.tracksIDsList) {
            updateUiState(
                inPlaylist = false,
                messageState = MessageState.ALREADY_ADDED,
                playlistName = playlist.name
            )
        } else {
            updatePlaylistAndTrack(playlist, currentTrack)
        }
    }

    private fun loadPlaylists() {
        viewModelScope.launch {
            interactorPlaylist.getAllPlaylists().collect { playlistList ->
                _playlists.postValue(playlistList)
            }
        }
    }

    private fun startPlayer() {
        interactorPlayer.startPlayer()
        startPlayingTimeCounter()
        updatePlayerState(PlayerState.PLAYING, getCurrentTime(), null)
    }

    private fun pausePlayer() {
        interactorPlayer.pausePlayer()
        stopPlayingTimeCounter()
        updatePlayerState(PlayerState.PAUSED, getCurrentTime(), null)
    }

    private fun startPlayingTimeCounter() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (interactorPlayer.isPlaying()) {
                updatePlayerState(PlayerState.PLAYING, getCurrentTime(), null)
                delay(TIMER_DELAY)
            }
        }
    }

    private fun stopPlayingTimeCounter() {
        timerJob?.cancel()
    }

    private fun getCurrentTime(): Long {
        return interactorPlayer.getCurrentPosition().toLong()
    }

    private fun convertTime(currentTime: Long): String {
        return if (currentTime == 0L) {
            "00:00"
        } else {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(currentTime)
            val seconds = TimeUnit.MILLISECONDS.toSeconds(currentTime) % 60
            String.format("%02d:%02d", minutes, seconds)
        }
    }

    fun reactOnLikeButton(currentTrack: PlayerTrack) {
        if (_uiState.value!!.isLiked) {
            deleteTrackFromFavorite(currentTrack)
            currentTrack.isLiked = false
        } else {
            addTrackToFavorite(currentTrack)
            currentTrack.isLiked = true
        }
        updateUiState(isLiked = currentTrack.isLiked)
    }

    fun checkInLiked(currentTrack: PlayerTrack) {
        viewModelScope.launch {
            val isLiked = interactorLikedTracks.getTracks()
                .map { tracks ->
                    tracks.any {
                        it.trackId == currentTrack.trackId
                    }
                }.first()
            updateUiState(isLiked)
        }
    }

    private fun updatePlaylistAndTrack(playlist: PlaylistEntityPresentation, track: PlayerTrack) {
        viewModelScope.launch {
            val result = interactorPlaylist.updatePlaylistAndTrack(playlist.id, track)
            val playlistName = playlist.name
            if (Result.success(Unit) == result) {
                updateUiState(
                    bottomSheet = BottomSheetBehavior.STATE_HIDDEN,
                    inPlaylist = true,
                    messageState = MessageState.SUCCESS,
                    playlistName = playlistName
                )
            } else {
                updateUiState(
                    bottomSheet = BottomSheetBehavior.STATE_HIDDEN,
                    inPlaylist = false,
                    messageState = MessageState.FAIL,
                    playlistName = playlistName

                )
            }
        }

    }

    private fun addTrackToFavorite(currentTrack: PlayerTrack) {
        viewModelScope.launch {
            interactorLikedTracks.addTrack(currentTrack)
        }
    }

    private fun deleteTrackFromFavorite(currentTrack: PlayerTrack) {
        viewModelScope.launch {
            interactorLikedTracks.deleteEntity(currentTrack)
        }
    }

    private fun updatePlayerState(
        state: PlayerState,
        currentTime: Long,
        errorMessage: String? = null
    ) {
        _viewState.postValue(PlayerViewState(state, convertTime(currentTime), errorMessage))
    }

    private fun updateUiState(
        isLiked: Boolean = _uiState.value?.isLiked ?: false,
        inPlaylist: Boolean = _uiState.value?.inPlaylist ?: false,
        bottomSheet: Int = _uiState.value?.bottomSheet ?: BottomSheetBehavior.STATE_HIDDEN,
        overlayVisibility: Boolean = _uiState.value?.overlayVisibility ?: false,
        messageState: MessageState = MessageState.NOTHING,
        playlistName: String = ""
    ) {
        _uiState.postValue(
            UiState(
                isLiked, inPlaylist, bottomSheet, overlayVisibility, messageState, playlistName
            )
        )
    }

    companion object {
        private const val TIMER_DELAY = 300L
    }
}


