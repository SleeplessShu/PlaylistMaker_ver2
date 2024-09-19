package com.practicum.playlistmaker_ver2.search.ui.models


import com.practicum.playlistmaker_ver2.search.domain.models.Track

data class SearchViewState(
    val state: SearchState = SearchState.EMPTY,
    val tracks: List<Track> = emptyList(),
    val errorMessage: String? = null
)
