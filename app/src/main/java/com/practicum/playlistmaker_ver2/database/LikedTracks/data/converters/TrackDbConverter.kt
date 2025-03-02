package com.practicum.playlistmaker_ver2.database.LikedTracks.data.converters


import com.practicum.playlistmaker_ver2.database.LikedTracks.data.TrackEntity
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerTrack
import com.practicum.playlistmaker_ver2.search.domain.models.Track

class TrackDbConverter {

    fun mapToList(track: TrackEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.artistName,
            track.trackTime,
            track.previewUrl,
            track.artworkUrl500,
            track.order

        )
    }

    fun mapToPlayer(track: TrackEntity): PlayerTrack {
        return PlayerTrack(
            track.trackId,
            track.trackName,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.artistName,
            track.trackTime,
            track.previewUrl,
            track.artworkUrl500,
            track.isLiked,
            track.order
        )
    }

    fun mapToTrackEntity(track: PlayerTrack): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.trackName,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.artistName,
            track.trackTime,
            track.previewUrl,
            getLowResCoverArtwork(track.artworkUrl500),
            track.isLiked

        )
    }

    private fun getLowResCoverArtwork(link: String): String {
        return link.replaceAfterLast('/', "100x100bb.jpg")
    }

}