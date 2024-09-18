package com.practicum.playlistmaker_ver2.player.ui

import com.practicum.playlistmaker_ver2.player.domain.models.PlayerState
import com.practicum.playlistmaker_ver2.search.domain.models.Track

data class PlayerViewState(
    val playerState: PlayerState = PlayerState.DEFAULT,
    val currentTime: String = "00:00",
    val errorMessage: String? = null
)