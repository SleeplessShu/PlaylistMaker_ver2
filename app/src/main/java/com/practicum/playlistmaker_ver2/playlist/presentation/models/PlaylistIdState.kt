package com.practicum.playlistmaker_ver2.playlist.presentation.models

import com.practicum.playlistmaker_ver2.playlist.data.database.entity.PlaylistEntity

interface PlaylistIdState {
    data class Content(val data: PlaylistEntity): PlaylistIdState
    data class Error(val error:String): PlaylistIdState

}