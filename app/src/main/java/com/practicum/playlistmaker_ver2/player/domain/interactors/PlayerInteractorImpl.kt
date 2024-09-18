package com.practicum.playlistmaker_ver2.player.domain.interactors

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker_ver2.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker_ver2.player.domain.models.PlayerState
import com.practicum.playlistmaker_ver2.player.domain.models.PlayerTrack
import okhttp3.internal.http2.Http2Reader

class PlayerInteractorImpl : PlayerInteractor {

    private var listener: ((PlayerState, Long, String?) -> Unit)? = null

    private var mediaPlayer: MediaPlayer? = null

    private var playingTimeCounter: Runnable? = null

    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private var isPlayerReady: Boolean = false
    private var savedTrackUrl = ""
    override fun setTrackUrl(trackUrl: String) {
        setupMediaPlayer(trackUrl)
        savedTrackUrl = trackUrl
    }

    override fun setStateChangeListener(listener: (PlayerState, Long, String?) -> Unit) {
        this.listener = listener
    }

    private fun setupMediaPlayer(url: String) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        }
        mediaPlayer = MediaPlayer().apply {
            reset()
            setDataSource(url)
            prepareAsync()

            setOnPreparedListener {
                isPlayerReady = true
                listener?.invoke(PlayerState.PREPARED, 0L, null)
            }

            setOnCompletionListener {

                stopPlayer()
                listener?.invoke(PlayerState.STOPPED, 0L, null)
            }

            setOnErrorListener { _, what, extra ->
                val errorMessage = "Error occurred: $what, $extra"
                listener?.invoke(PlayerState.ERROR, 0L, errorMessage)
                true
            }
        }
    }

    override fun playPause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                pausePlayer()
            } else {
                startPlayer()
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer?.start()
        startPlayingTimeCounter()
        val currentTime = getCurrentTime()
        listener?.invoke(PlayerState.PLAYING, currentTime, null)
    }

    private fun pausePlayer() {
        mediaPlayer?.pause()
        val currentTime = getCurrentTime()
        stopPlayingTimeCounter()
        listener?.invoke(PlayerState.PAUSED, currentTime, null)
    }

    private fun stopPlayer() {
        isPlayerReady = false
        stopPlayingTimeCounter()
        mediaPlayer?.stop()
        Log.d("STATE", "stop(): ")
        listener?.invoke(PlayerState.STOPPED, 0L, null)
        setupMediaPlayer(savedTrackUrl)
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
                listener?.invoke(PlayerState.PLAYING, currentTime, null)
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

    override fun release() {
        isPlayerReady = false
        mediaPlayer?.release()
        mediaPlayer = null
    }

    companion object {
        private const val DELAY = 1000L
    }
}
