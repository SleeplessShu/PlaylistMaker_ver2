package com.practicum.playlistmaker_ver2.domain.api

import com.practicum.playlistmaker_ver2.domain.models.Track

fun interface TrackConsumer {
    fun consume(foundTracks: List<Track>?, errorMessage: String?)
}
