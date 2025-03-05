package com.practicum.playlistmaker_ver2.mediateka.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker_ver2.player.domain.api.LikedTracksInteractor
import com.practicum.playlistmaker_ver2.mediateka.presentation.states.FavoriteTracksState
import com.practicum.playlistmaker_ver2.search.domain.models.Track
import kotlinx.coroutines.launch

class TabFavoriteViewModel(
    private val likedTracksInteractor: LikedTracksInteractor
) : ViewModel() {
    private val _stateLiveData = MutableLiveData<FavoriteTracksState>()
    val stateLiveData: LiveData<FavoriteTracksState> get() = _stateLiveData

    init {
        observeFavoriteTracks()
    }

    private fun observeFavoriteTracks() {
        renderState(FavoriteTracksState.Loading)
        viewModelScope.launch {
            likedTracksInteractor
                .getTracks()
                .collect { tracks ->
                    processResult(tracks)
                }
        }
    }

    private fun processResult(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            renderState((FavoriteTracksState.Empty("")))
        } else {
            renderState(FavoriteTracksState.Content(tracks))
        }
    }

    private fun renderState(state: FavoriteTracksState) {
        _stateLiveData.postValue(state)
    }
}