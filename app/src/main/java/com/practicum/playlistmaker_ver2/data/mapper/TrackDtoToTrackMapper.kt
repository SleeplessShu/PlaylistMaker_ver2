package com.practicum.playlistmaker_ver2.data.mapper


import com.practicum.playlistmaker_ver2.data.dto.TrackDto
import com.practicum.playlistmaker_ver2.domain.models.Track
import java.util.concurrent.TimeUnit

object TrackDtoToTrackMapper {
    fun map(trackDto: List<TrackDto>): List<Track> {
        return trackDto.map {
            Track(
                it.trackId,
                it.trackName,
                it.collectionName,
                it.releaseDate,
                it.primaryGenreName,
                it.country,
                it.artistName,
                formatTrackDuration(it.trackTimeMillis),
                it.previewUrl,
                it.artworkUrl100
            )
        }
    }

    private fun formatTrackDuration(trackTimeMillis: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(trackTimeMillis)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(trackTimeMillis) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}