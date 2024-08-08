package com.practicum.playlistmaker_ver2.domain.api

import com.practicum.playlistmaker_ver2.domain.models.Track

interface TracksInteractor {
    fun searchTrack(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTracks: List<Track>?, errorMessage: String?)
    }
}