package com.practicum.playlistmaker_ver2.player.domain.api

interface PlayerInteractor {
    fun prepare(
        trackUrl: String,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit,
        onError: (what: Int, extra: Int) -> Boolean
    )

    fun startPlayer()
    fun pausePlayer()
    fun stopPlayer()
    fun releasePlayer()
    fun isPlaying(): Boolean
    fun getCurrentPosition(): Int
    fun seekTo(position: Int)
}