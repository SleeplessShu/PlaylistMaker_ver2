package com.practicum.playlistmaker_ver2.playlist.presentation

sealed class LayoutType {
    object ForPlayer: LayoutType()
    object  ForPlaylist: LayoutType()
}