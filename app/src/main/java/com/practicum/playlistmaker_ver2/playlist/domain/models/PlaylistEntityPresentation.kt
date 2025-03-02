package com.practicum.playlistmaker_ver2.playlist.domain.models

data class PlaylistEntityPresentation (
    val id: Int,
    val name: String,
    val description: String,
    val image: String,
    val tracksIDsList: String,
    val tracksCount: Int,
)
