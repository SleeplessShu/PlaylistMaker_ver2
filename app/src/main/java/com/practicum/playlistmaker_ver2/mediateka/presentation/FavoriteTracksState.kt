package com.practicum.playlistmaker_ver2.mediateka.presentation

import com.practicum.playlistmaker_ver2.search.domain.models.Track

sealed interface FavoriteTracksState {

    object Loading : FavoriteTracksState
    data class Content(
        val tracks: List<Track>
    ) : FavoriteTracksState

    data class Empty(
        val message: String
    ) : FavoriteTracksState

}