package com.practicum.playlistmaker_ver2.player.ui

import com.practicum.playlistmaker_ver2.player.ui.models.PlayerState

data class PlayerViewState(
    val playerState: PlayerState = PlayerState.DEFAULT,
    val currentTime: String = "00:00",
    val errorMessage: String? = null
)