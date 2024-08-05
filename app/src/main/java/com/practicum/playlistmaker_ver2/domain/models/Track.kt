package com.practicum.playlistmaker_ver2.domain.models

import java.io.Serializable

interface Track {
    val trackId: Int
    val trackName: String
    val collectionName: String
    val releaseDate: String
    val primaryGenreName: String
    val country: String
    val artistName: String
    val trackTimeMillis: Long
    val previewUrl: String
    val artworkUrl100: String
}