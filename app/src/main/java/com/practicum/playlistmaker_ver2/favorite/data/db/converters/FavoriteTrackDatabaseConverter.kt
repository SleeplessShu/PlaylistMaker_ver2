package com.practicum.playlistmaker_ver2.favorite.data.db.converters

import com.practicum.playlistmaker_ver2.favorite.data.db.entity.FavoriteEntity
import com.practicum.playlistmaker_ver2.search.domain.models.Track

class FavoriteTrackDatabaseConverter {
    fun map(track: FavoriteEntity): Track {
        return Track(track.trackId.toInt(), track.trackName, track.country,
            track.releaseDate, track.collectionName,track.primaryGenreName, track.artistName, track.trackTimeMillis,
            track.previewUrl, track.artworkUrl100,  track.inFavorite, System.currentTimeMillis())
    }
}