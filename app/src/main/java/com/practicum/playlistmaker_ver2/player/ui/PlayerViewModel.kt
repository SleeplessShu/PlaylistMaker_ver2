package com.practicum.playlistmaker_ver2.player.ui


import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerState
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerTrack
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerViewState
import java.util.concurrent.TimeUnit

class PlayerViewModel(
    private val track: PlayerTrack
) : ViewModel() {

    private var playingTimeCounter: Runnable? = null
    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private var viewState = MutableLiveData(PlayerViewState())
    private var savedTrackUrl = ""
    private var mediaPlayer: MediaPlayer? = null
    private var isPlayerReady: Boolean = false

    fun getViewState(): LiveData<PlayerViewState> = viewState

    fun initializePlayer(currentTrack: PlayerTrack) {
        setupMediaPlayer(currentTrack.previewUrl)
        savedTrackUrl = currentTrack.previewUrl
    }

    fun playPause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                pausePlayer()
            } else {
                startPlayer()
            }
        }
    }

    fun releasePlayer() {
        isPlayerReady = false
        mediaPlayer?.release()
        mediaPlayer = null
        stopPlayingTimeCounter()
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }

    private fun setupMediaPlayer(url: String) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        } else {
            mediaPlayer?.reset()
        }
        mediaPlayer?.apply {

            setDataSource(url)
            prepareAsync()

            setOnPreparedListener {
                isPlayerReady = true
                updatePlayerState(PlayerState.PREPARED, 0L, null)
            }

            setOnCompletionListener {
                stopPlayer()
                updatePlayerState(PlayerState.STOPPED, 0L, null)
            }

            setOnErrorListener { _, what, extra ->
                val errorMessage = "Error occurred: $what, $extra"
                updatePlayerState(PlayerState.ERROR, 0L, errorMessage)
                true
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer?.start()
        startPlayingTimeCounter()
        val currentTime = getCurrentTime()
        updatePlayerState(PlayerState.PLAYING, currentTime, null)
    }

    private fun stopPlayer() {
        isPlayerReady = false
        stopPlayingTimeCounter()
        mediaPlayer?.stop()
        updatePlayerState(PlayerState.STOPPED, 0L, null)
        setupMediaPlayer(savedTrackUrl)
    }

    private fun pausePlayer() {
        mediaPlayer?.pause()
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
        return if (isPlayerReady && mediaPlayer != null) {
            mediaPlayer!!.currentPosition.toLong()
        } else {
            0L
        }
    }

    private fun convertTime(currentTime: Long): String {
        return if (currentTime == 0L) {
            "00:00"
        } else {
            val hours = TimeUnit.MILLISECONDS.toHours(currentTime)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(currentTime) % 60
            val seconds = TimeUnit.MILLISECONDS.toSeconds(currentTime) % 60
            if (hours > 0) {
                String.format("%02d:%02d:%02d", hours, minutes, seconds)
            } else {
                String.format("%02d:%02d", minutes, seconds)
            }
        }
    }

    private fun updatePlayerState(
        state: PlayerState,
        currentTime: Long,
        errorMessage: String? = null
    ) {
        viewState.postValue(PlayerViewState(state, convertTime(currentTime), errorMessage))
    }

    companion object {
        fun provideFactory(
            track: PlayerTrack
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
                    return PlayerViewModel(track) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        private const val DELAY = 1000L
    }
}
