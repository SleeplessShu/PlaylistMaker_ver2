package com.practicum.playlistmaker_ver2.search.domain.models

sealed class ResponsePair<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : ResponsePair<T>(data)
    class Error<T>(message: String, data: T? = null) : ResponsePair<T>(data, message)
}