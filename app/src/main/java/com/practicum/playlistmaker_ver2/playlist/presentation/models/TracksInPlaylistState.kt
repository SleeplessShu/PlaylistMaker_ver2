package com.practicum.playlistmaker_ver2.playlist.presentation.models

import com.practicum.playlistmaker_ver2.player.ui.models.MessageState
import com.practicum.playlistmaker_ver2.search.domain.models.Track

sealed interface TracksInPlaylistState {

    data class Content(
        val tracks: List<Track>,
        val message: MessageState
    ) : TracksInPlaylistState

    data class Empty(
        val message: MessageState
    ) : TracksInPlaylistState

}