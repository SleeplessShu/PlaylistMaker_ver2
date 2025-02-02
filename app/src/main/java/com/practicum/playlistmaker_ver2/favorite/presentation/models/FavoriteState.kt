package com.practicum.playlistmaker_ver2.favorite.presentation.models

import com.practicum.playlistmaker_ver2.search.domain.models.Track

sealed interface FavoriteState {

    data class Content(val data : List<Track>) : FavoriteState
    data class Error(val message : String): FavoriteState
}