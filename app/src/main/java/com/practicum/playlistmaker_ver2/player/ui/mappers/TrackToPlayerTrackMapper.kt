package com.practicum.playlistmaker_ver2.player.ui.mappers

import com.practicum.playlistmaker_ver2.player.ui.models.PlayerTrack
import com.practicum.playlistmaker_ver2.search.domain.models.Track

object TrackToPlayerTrackMapper {
    fun map(track: Track): PlayerTrack {
        return PlayerTrack(
            track.trackId,
            track.trackName,
            track.collectionName,
            track.releaseDate.substring(0, 4),
            track.primaryGenreName,
            track.country,
            track.artistName,
            track.trackTime,
            track.previewUrl ?: "",
            getHiResCoverArtwork(track.artworkUrl100)
        )
    }

    private fun getHiResCoverArtwork(link: String): String {
        return link.replaceAfterLast('/', "512x512bb.jpg")
    }
}