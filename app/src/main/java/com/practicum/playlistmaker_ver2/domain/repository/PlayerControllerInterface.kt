package com.practicum.playlistmaker_ver2.domain.repository

import com.practicum.playlistmaker_ver2.domain.models.Track

interface PlayerControllerInterface {
    fun onCreate(currentTrack: Track)
    fun onPause()
    fun onStop()
    fun onDestroy()
    fun playbackControl()
}
