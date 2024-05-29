package com.practicum.playlistmaker_ver2

data class TrackData(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String
)