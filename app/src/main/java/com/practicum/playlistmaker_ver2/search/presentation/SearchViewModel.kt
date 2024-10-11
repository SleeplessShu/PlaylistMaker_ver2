package com.practicum.playlistmaker_ver2.search.presentation


import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker_ver2.search.domain.interactor.SearchInteractor
import com.practicum.playlistmaker_ver2.search.domain.models.Track
import com.practicum.playlistmaker_ver2.search.presentation.models.SearchState
import com.practicum.playlistmaker_ver2.search.presentation.models.SearchViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val handler: Handler

) : ViewModel() {
    var currentQuery: String = ""
    private var searchJob: Job? = null
    private val _searchViewState = MutableLiveData<SearchViewState>()
    val searchViewState: LiveData<SearchViewState> get() = _searchViewState


    init {
        _searchViewState.value = SearchViewState()
        if (currentQuery.isEmpty()) {
            loadSearchHistory()

        } else {
            searchTracks(currentQuery)
        }
    }


    fun onSearchQueryChanged(query: String) {
        currentQuery = query
    }


    fun searchTracks(query: String) {
        if (query.isEmpty()) {
            loadSearchHistory()
            return
        }
        _searchViewState.postValue(_searchViewState.value?.copy(state = SearchState.SEARCHING))

        searchInteractor.searchTracks(query) { tracks, errorMessage ->
            if (errorMessage != null) {
                _searchViewState.postValue(
                    _searchViewState.value?.copy(
                        state = SearchState.NO_INTERNET, errorMessage = errorMessage
                    )
                )
            } else if (tracks?.isEmpty() == true) {
                _searchViewState.postValue(
                    _searchViewState.value?.copy(state = SearchState.NOTHING_FOUND)
                )
            } else {
                tracks?.let {
                    _searchViewState.postValue(
                        _searchViewState.value?.copy(
                            state = SearchState.TRACKS, tracks = tracks
                        )
                    )
                }
            }

        }
    }

    fun searchDebounce() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(DEBOUNCE_DELAY)
            searchTracks(currentQuery)
        }

    }

    fun clearSearchHistory() {
        searchInteractor.clearSearchHistory()
        _searchViewState.postValue(_searchViewState.value?.copy(state = SearchState.EMPTY))
    }

    fun addToSearchHistory(track: Track) {
        searchInteractor.addTrackToHistory(track)
        if (currentQuery.isEmpty()) {
            loadSearchHistory()
        }

    }

    fun restoreSearchState(searchText: String?) {
        searchText?.let {
            currentQuery = it
        }
    }

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }

    private fun loadSearchHistory() {
        val history = searchInteractor.getSearchHistory()
        if (history.isNotEmpty()) {
            _searchViewState.postValue(
                _searchViewState.value?.copy(
                    state = SearchState.HISTORY,
                    tracks = history,
                )
            )
        } else {
            _searchViewState.postValue(_searchViewState.value?.copy(state = SearchState.EMPTY))
        }

    }

    private companion object {
        const val DEBOUNCE_DELAY = 1000L
    }
}
