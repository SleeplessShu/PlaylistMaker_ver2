package com.practicum.playlistmaker_ver2.search.data.mapper


import com.practicum.playlistmaker_ver2.search.data.dto.TrackDto
import com.practicum.playlistmaker_ver2.search.domain.models.Track
import java.util.concurrent.TimeUnit

object TrackDtoToTrackMapper {
    fun map(trackDto: List<TrackDto>): List<Track> {
        return trackDto.map {
            Track(
                it.trackId,
                it.trackName ?: "Unknown",
                it.collectionName ?: "Unknown",
                it.releaseDate ?: "Unknown",
                it.primaryGenreName ?: "Unknown",
                it.country ?: "Unknown",
                it.artistName ?: "Unknown",
                formatTrackDuration(it.trackTimeMillis) ?: "00:00",
                it.previewUrl ?: "",
                it.artworkUrl100 ?: ""
            )
        }
    }

    private fun formatTrackDuration(trackTimeMillis: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(trackTimeMillis)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(trackTimeMillis) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}