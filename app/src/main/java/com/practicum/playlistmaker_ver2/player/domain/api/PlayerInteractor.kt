package com.practicum.playlistmaker_ver2.player.domain.api

import androidx.lifecycle.LiveData
import com.practicum.playlistmaker_ver2.player.domain.models.PlayerState
import com.practicum.playlistmaker_ver2.player.domain.models.PlayerTrack

interface PlayerInteractor {
    val playerTrack: LiveData<PlayerTrack>
    val playerState: LiveData<PlayerState>
    val currentTime: LiveData<Long>
    val errorMessage: LiveData<String>
    val isPlaying: LiveData<Boolean>
    fun playPause()
    fun stop()
    fun release()
    fun setTrack(playerTrack: PlayerTrack)
}
