package com.practicum.playlistmaker_ver2.search.domain.api

interface TrackInteractor {
    fun doRequest(expression: String, consumer: TrackConsumer)
}