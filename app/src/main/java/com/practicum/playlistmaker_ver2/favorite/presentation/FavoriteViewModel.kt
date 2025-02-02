package com.practicum.playlistmaker_ver2.favorite.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.favorite.domain.interactors.FavoriteInteractor
import com.practicum.playlistmaker_ver2.favorite.presentation.models.FavoriteState
import com.practicum.playlistmaker_ver2.search.domain.models.Track
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val favoriteInteractor: FavoriteInteractor,
    private val context: Context
) : ViewModel() {

    private val screenState = MutableLiveData<FavoriteState>()
    fun getScreenState(): LiveData<FavoriteState> = screenState


    fun fillData() {

        viewModelScope.launch {
            favoriteInteractor.getFavoriteTrack().collect { track ->
                processResult(track)
            }
        }
    }

    fun refreshItem() {
        fillData()
    }

    private fun processResult(track: List<Track>) {
        if (track.isEmpty()) {
            renderState(FavoriteState.Error(context.getString(R.string.mediaEmpty)))
        } else {
            renderState(FavoriteState.Content(track))
        }
    }


    private fun renderState(state: FavoriteState) {
        screenState.postValue(state)
    }
}