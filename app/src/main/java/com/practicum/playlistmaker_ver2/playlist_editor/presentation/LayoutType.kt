package com.practicum.playlistmaker_ver2.playlist_editor.presentation

sealed class LayoutType {
    object ForPlayer: LayoutType()
    object  ForPlaylist: LayoutType()
}