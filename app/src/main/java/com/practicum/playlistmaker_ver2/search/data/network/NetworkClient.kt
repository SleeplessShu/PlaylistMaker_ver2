package com.practicum.playlistmaker_ver2.search.data.network

import com.practicum.playlistmaker_ver2.search.data.dto.Response


interface NetworkClient {
    fun doRequest(dto: Any): Response
}
