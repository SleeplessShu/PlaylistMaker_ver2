package com.practicum.playlistmaker_ver2.search.domain.api

import com.practicum.playlistmaker_ver2.search.domain.models.Track

fun interface TrackConsumer {
    fun consume(foundTracks: List<Track>?, errorMessage: String?)
}
