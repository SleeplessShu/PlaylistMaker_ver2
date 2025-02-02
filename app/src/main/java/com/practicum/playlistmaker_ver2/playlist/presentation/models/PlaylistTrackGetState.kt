package com.practicum.playlistmaker_ver2.playlist.presentation.models

import com.practicum.playlistmaker_ver2.playlist.data.database.entity.PlaylistTrackEntity

sealed  interface PlaylistTrackGetState {
    data class Content(val data:List<PlaylistTrackEntity>): PlaylistTrackGetState
    data class Error(val message:String): PlaylistTrackGetState
}