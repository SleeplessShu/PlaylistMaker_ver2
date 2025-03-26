package com.practicum.playlistmaker_ver2.search.domain.models

import com.practicum.playlistmaker_ver2.player.ui.models.PlayerTrack
import com.practicum.playlistmaker_ver2.playlist_editor.data.entities.TrackInPlaylistEntity
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
    val isLiked: Boolean = false,
    val order: Int = 0
) : Serializable
fun Track.toPlayerTrack(): PlayerTrack {
    return PlayerTrack(
        trackId,
        trackName,
        collectionName,
        releaseDate,
        primaryGenreName,
        country,
        artistName,
        trackTime,
        previewUrl.toString(),
        artworkUrl100,
        isLiked,
        order
    )
}