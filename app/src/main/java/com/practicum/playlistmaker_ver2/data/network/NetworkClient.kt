package com.practicum.playlistmaker_ver2.data.network

import com.practicum.playlistmaker_ver2.data.dto.Response


interface NetworkClient {
    fun doRequest(dto: Any): Response
}
