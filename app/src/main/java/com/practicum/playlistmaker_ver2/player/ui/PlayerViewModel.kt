package com.practicum.playlistmaker_ver2.player.ui

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker_ver2.database.domain.LikedTracksInteractor
import com.practicum.playlistmaker_ver2.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerState
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerTrack
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerViewState
import com.practicum.playlistmaker_ver2.player.ui.models.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class PlayerViewModel(
    private val interactorPlayer: PlayerInteractor,
    private val interactorLikedTracks: LikedTracksInteractor,
    private val mainThreadHandler: Handler,
) : ViewModel() {


    private val viewState = MutableLiveData(PlayerViewState())
    private val uiState = MutableLiveData(UiState())
    private var savedTrackUrl = ""
    private var timerJob: Job? = null

    fun getViewState(): LiveData<PlayerViewState> = viewState
    fun getUiState(): LiveData<UiState> = uiState
    fun playPause() {
        if (interactorPlayer.isPlaying()) {
            pausePlayer()
        } else {
            startPlayer()
        }
    }

    fun releasePlayer() {
        interactorPlayer.releasePlayer()
        stopPlayingTimeCounter()
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        releasePlayer()
    }

    fun setupPlayer(trackUrl: String) {
        savedTrackUrl = trackUrl

        interactorPlayer.prepare(
            trackUrl, onPrepared = {
            updatePlayerState(PlayerState.PREPARED, 0L, null)
        },

            onCompletion = {
                mainThreadHandler.post {
                    stopPlayingTimeCounter()
                    interactorPlayer.seekTo(0)  // Reset to the beginning
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

    fun toggleLikeButton(currentTrack: PlayerTrack) {
        if (currentTrack.isLiked) {

            deleteTrackFromFavorite(currentTrack)
            currentTrack.isLiked = false

        } else {

            addTrackToFavorite(currentTrack)
            currentTrack.isLiked = true

        }
        updateUiState(currentTrack.isLiked)
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


    fun checkInLiked(track: PlayerTrack) {
        viewModelScope.launch {
            val isLiked = interactorLikedTracks.getTracks()
                .map { tracks -> tracks.any() { it.trackId == track.trackId } }
                .first()
            updateUiState(isLiked)
        }
    }

    private fun startPlayer() {
        interactorPlayer.startPlayer()
        startPlayingTimeCounter()
        val currentTime = getCurrentTime()
        updatePlayerState(PlayerState.PLAYING, currentTime, null)
    }

    private fun pausePlayer() {
        interactorPlayer.pausePlayer()
        val currentTime = getCurrentTime()
        stopPlayingTimeCounter()
        updatePlayerState(PlayerState.PAUSED, currentTime, null)
    }

    private fun startPlayingTimeCounter() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (interactorPlayer.isPlaying()) {
                val currentTime = getCurrentTime()
                updatePlayerState(PlayerState.PLAYING, currentTime, null)
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

    private fun updatePlayerState(
        state: PlayerState, currentTime: Long, errorMessage: String? = null
    ) {
        viewState.postValue(PlayerViewState(state, convertTime(currentTime), errorMessage))
    }

    private fun updateUiState(
        isLiked: Boolean
    ) {
        uiState.postValue(UiState(isLiked))
    }

    companion object {
        private const val TIMER_DELAY = 300L
    }
}
