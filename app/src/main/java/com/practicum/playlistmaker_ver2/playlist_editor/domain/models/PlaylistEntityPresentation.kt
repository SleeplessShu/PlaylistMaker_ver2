package com.practicum.playlistmaker_ver2.playlist_editor.domain.models

import com.practicum.playlistmaker_ver2.utils.Constants

data class PlaylistEntityPresentation (
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val image: String = Constants.DEFAULT_PLAYLIST_IMAGE_URI.toString(),
    val tracksIDsList: String = "",
    val tracksCount: Int = 0,
    val tracksDuration: String = "default value"
)
