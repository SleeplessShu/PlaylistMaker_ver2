package com.practicum.playlistmaker_ver2.player.domain.impl

import com.practicum.playlistmaker_ver2.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker_ver2.player.domain.repositories.PlayerRepository

class PlayerInteractorImpl(private val playerRepository: PlayerRepository) : PlayerInteractor {

    override fun prepare(
        trackUrl: String,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit,
        onError: (what: Int, extra: Int) -> Boolean
    ) {
        playerRepository.prepare(trackUrl, onPrepared, onCompletion, onError)
    }

    override fun startPlayer() {
        playerRepository.startPlayer()
    }

    override fun pausePlayer() {
        playerRepository.pausePlayer()
    }

    override fun stopPlayer() {
        playerRepository.stopPlayer()
    }

    override fun releasePlayer() {
        playerRepository.releasePlayer()
    }

    override fun isPlaying(): Boolean {
        return playerRepository.isPlaying()
    }

    override fun getCurrentPosition(): Int {
        return playerRepository.getCurrentPosition()
    }

    override fun seekTo(position: Int) {
        playerRepository.seekTo(position)
    }

}
