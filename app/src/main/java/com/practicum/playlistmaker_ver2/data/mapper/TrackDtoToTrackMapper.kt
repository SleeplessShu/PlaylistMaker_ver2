package com.practicum.playlistmaker_ver2.data.mapper

import android.util.Log
import com.practicum.playlistmaker_ver2.data.dto.TrackDto
import com.practicum.playlistmaker_ver2.domain.models.Track

object TrackDtoToTrackMapper {
    fun map(trackDto: List<TrackDto>): List<Track> {
        Log.d("shu", "TrackDtoToTrackMapper")
        return trackDto.map {
            Track(
                it.trackId,
                it.trackName,
                it.collectionName,
                it.releaseDate,
                it.primaryGenreName,
                it.country,
                it.artistName,
                formatMillisToMinutesSeconds(it.trackTimeMillis),
                it.previewUrl,
                it.artworkUrl100
            )
        }
    }

    private fun formatMillisToMinutesSeconds(trackTimeMillis: Long): String {
        val minutes = trackTimeMillis / 1000 / 60
        val seconds = (trackTimeMillis / 1000 % 60)
        return String.format("%02d:%02d", minutes, seconds)
    }
}