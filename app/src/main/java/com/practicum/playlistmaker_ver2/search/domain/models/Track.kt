package com.practicum.playlistmaker_ver2.search.domain.models

import java.io.Serializable


data class Track(
    val trackId: Int,
    val trackName: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val artistName: String,
    val trackTime: String,
    val previewUrl: String?,
    val artworkUrl100: String,
    val order: Int = 0
) : Serializable