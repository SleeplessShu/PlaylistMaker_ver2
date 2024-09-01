package com.practicum.playlistmaker_ver2.old.data.network

import com.practicum.playlistmaker_ver2.old.data.dto.Response


interface NetworkClient {
    fun doRequest(dto: Any): Response
}
