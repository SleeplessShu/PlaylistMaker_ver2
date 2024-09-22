package com.practicum.playlistmaker_ver2.search.domain.interactor

import com.practicum.playlistmaker_ver2.search.domain.api.TrackConsumer

interface TrackInteractor {
    fun doRequest(expression: String, consumer: TrackConsumer)
}