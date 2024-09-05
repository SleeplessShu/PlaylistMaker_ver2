package com.practicum.playlistmaker_ver2.player.domain.interactors

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker_ver2.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker_ver2.player.domain.models.PlayerState
import com.practicum.playlistmaker_ver2.player.domain.models.PlayerTrack

class PlayerInteractorImpl : PlayerInteractor {

    private var mediaPlayer: MediaPlayer? = null

    private val _playerTrack = MutableLiveData<PlayerTrack>()
    override val playerTrack: LiveData<PlayerTrack> get() = _playerTrack

    private val _isPlaying = MutableLiveData<Boolean>()
    override val isPlaying: LiveData<Boolean> get() = _isPlaying

    private val _currentTime = MutableLiveData<Long>()
    override val currentTime: LiveData<Long> get() = _currentTime

    private val _playerState = MutableLiveData<PlayerState>()
    override val playerState: LiveData<PlayerState> get() = _playerState

    private val _errorMessage = MutableLiveData<String>()
    override val errorMessage: LiveData<String> get() = _errorMessage

    private var playingTimeCounter: Runnable? = null
    private val handler = android.os.Handler()

    override fun setTrack(playerTrack: PlayerTrack) {
        _playerTrack.postValue(playerTrack)
        setupMediaPlayer(playerTrack.previewUrl)
    }

    private fun setupMediaPlayer(url: String) {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener {
                _isPlaying.postValue(false)
                _playerState.postValue(PlayerState.PREPARED)
            }
            setOnCompletionListener {
                _isPlaying.postValue(false)
                _playerState.postValue(PlayerState.STOPPED)
                stopPlayingTimeCounter()
                _currentTime.postValue(0L)
            }
            setOnErrorListener { _, what, extra ->
                _errorMessage.postValue("Error occurred: $what, $extra")
                _playerState.postValue(PlayerState.ERROR)
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
        _isPlaying.postValue(true)
        _playerState.postValue(PlayerState.PLAYING)
        startPlayingTimeCounter()
    }

    private fun pausePlayer() {
        mediaPlayer?.pause()
        _isPlaying.postValue(false)
        _playerState.postValue(PlayerState.PAUSED)
        stopPlayingTimeCounter()
    }

    private fun stopPlayingTimeCounter() {
        playingTimeCounter?.let { handler.removeCallbacks(it) }
    }

    private fun startPlayingTimeCounter() {
        playingTimeCounter = object : Runnable {
            override fun run() {
                mediaPlayer?.currentPosition?.toLong()?.let { _currentTime.postValue(it) }
                handler.postDelayed(this, 200L)
            }
        }
        handler.post(playingTimeCounter!!)
    }

    override fun stop() {
        mediaPlayer?.stop()
        _isPlaying.postValue(false)
        _playerState.postValue(PlayerState.STOPPED)
        stopPlayingTimeCounter()
        _currentTime.postValue(0L)
    }

    override fun release() {
        mediaPlayer?.release()
        stopPlayingTimeCounter()
    }
}
