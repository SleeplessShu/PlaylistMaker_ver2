package com.practicum.playlistmaker_ver2.playlist.presentation


data class PlaylistEntityPresentationShort(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val image: String = "",
    val tracksIDsList: String = "",
    val tracksCount: Int = 0,
    val playlistDuration: String = ""

)