package com.practicum.playlistmaker_ver2.player.domain.api


import com.practicum.playlistmaker_ver2.player.domain.models.PlayerState


interface PlayerInteractor {

    fun playPause()

    fun release()
    fun setTrackUrl(trackUrl: String)
    fun setStateChangeListener(listener: (PlayerState, Long, String?) -> Unit)
}


