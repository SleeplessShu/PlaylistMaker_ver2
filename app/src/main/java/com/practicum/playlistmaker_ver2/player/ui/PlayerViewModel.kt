package com.practicum.playlistmaker_ver2.player.ui

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker_ver2.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerState
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerViewState
import java.util.concurrent.TimeUnit

class PlayerViewModel(
    private val interactor: PlayerInteractor,
    private val mainThreadHandler: Handler,
) : ViewModel() {

    private var playingTimeCounter: Runnable? = null
    private val viewState = MutableLiveData(PlayerViewState())
    private var savedTrackUrl = ""

    fun getViewState(): LiveData<PlayerViewState> = viewState

    fun playPause() {
        if (interactor.isPlaying()) {
            pausePlayer()
        } else {
            startPlayer()
        }
    }

    fun releasePlayer() {
        interactor.releasePlayer()
        stopPlayingTimeCounter()
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }

    fun setupPlayer(trackUrl: String) {
        savedTrackUrl = trackUrl

        interactor.prepare(trackUrl, onPrepared = {
            updatePlayerState(PlayerState.PREPARED, 0L, null)
        },

            onCompletion = {
                mainThreadHandler.post {
                    stopPlayingTimeCounter()
                    interactor.seekTo(0)  // Reset to the beginning
                    updatePlayerState(PlayerState.PREPARED, 0L, null)
                }
            },

            onError = { what, extra ->
                val errorMessage = "Error occurred: $what, $extra"
                mainThreadHandler.post {
                    updatePlayerState(PlayerState.ERROR, 0L, errorMessage)
                }
                true
            }
        )
    }

    private fun startPlayer() {
        interactor.startPlayer()
        startPlayingTimeCounter()
        val currentTime = getCurrentTime()
        updatePlayerState(PlayerState.PLAYING, currentTime, null)
    }

    private fun pausePlayer() {
        interactor.pausePlayer()
        val currentTime = getCurrentTime()
        stopPlayingTimeCounter()
        updatePlayerState(PlayerState.PAUSED, currentTime, null)
    }

    private fun startPlayingTimeCounter() {
        playingTimeCounter = createPlayingTimeCounterTask()
        mainThreadHandler.post(playingTimeCounter!!)
    }

    private fun stopPlayingTimeCounter() {
        playingTimeCounter?.let { mainThreadHandler.removeCallbacks(it) }
    }

    private fun createPlayingTimeCounterTask(): Runnable {
        return object : Runnable {
            override fun run() {
                val currentTime = getCurrentTime()
                updatePlayerState(PlayerState.PLAYING, currentTime, null)
                mainThreadHandler.postDelayed(this, DELAY)
            }
        }
    }

    private fun getCurrentTime(): Long {
        return interactor.getCurrentPosition().toLong()
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

    companion object {
        private const val DELAY = 1000L
    }
}
