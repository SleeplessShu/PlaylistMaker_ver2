package com.practicum.playlistmaker_ver2.playlist.presentation.models

import com.practicum.playlistmaker_ver2.playlist.data.database.entity.PlaylistEntity

interface ListPlaylistState {

    data class Content(val data: List<PlaylistEntity>): ListPlaylistState
    data class Error(val error:String): ListPlaylistState
}