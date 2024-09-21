package com.practicum.playlistmaker_ver2.search.domain.api

import com.practicum.playlistmaker_ver2.search.domain.models.Track

interface SearchInteractor {
    fun searchTracks(expression: String, callback: (List<Track>?, String?) -> Unit)
    fun addTrackToHistory(track: Track)
    fun getSearchHistory(): List<Track>
    fun clearSearchHistory()
}
