package com.practicum.playlistmaker_ver2.playlist.presentation.models

import com.practicum.playlistmaker_ver2.playlist.data.database.entity.PlaylistEntity

sealed interface PlaylistState {
    data class Content(val data:List<PlaylistEntity>):PlaylistState
    data class Error(val message:String):PlaylistState
}