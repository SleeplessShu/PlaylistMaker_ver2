package com.practicum.playlistmaker_ver2.player.domain.repositories

interface PlayerRepository {
    fun prepare(
        trackUrl: String,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit,
        onError: (what: Int, extra: Int) -> Boolean
    )

    fun startPlayer()
    fun pausePlayer()
    fun stopPlayer()
    fun resetPlayer()
    fun releasePlayer()
    fun isPlaying(): Boolean
    fun isPrepared(): Boolean
    fun getCurrentPosition(): Int
    fun seekTo(position: Int)
}