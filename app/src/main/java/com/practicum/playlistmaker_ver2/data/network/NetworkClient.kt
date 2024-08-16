package com.practicum.playlistmaker_ver2.data.network

import com.practicum.playlistmaker_ver2.domain.api.TrackConsumer

interface NetworkClient {
    fun doRequest(expression: String, consumer: TrackConsumer)

}