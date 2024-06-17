package com.practicum.playlistmaker_ver2

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrackData(
    val trackId: Int,
    val trackName: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String
) : Parcelable