package com.practicum.playlistmaker_ver2.old.domain.api

interface TrackInteractor {
    fun doRequest(expression: String, consumer: TrackConsumer)
}