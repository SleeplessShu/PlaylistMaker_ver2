package com.practicum.playlistmaker_ver2.player.domain.models

data class PlayerTrack(
    val trackId: Int,
    val trackName: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val artistName: String,
    val trackTime: String,
    val previewUrl: String,
    val artworkUrl500: String
)