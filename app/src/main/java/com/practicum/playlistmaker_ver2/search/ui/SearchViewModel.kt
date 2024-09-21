package com.practicum.playlistmaker_ver2.search.ui



import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker_ver2.search.domain.api.SearchInteractor
import com.practicum.playlistmaker_ver2.search.domain.models.Track
import com.practicum.playlistmaker_ver2.search.ui.models.SearchState

import com.practicum.playlistmaker_ver2.search.ui.models.SearchViewState

class SearchViewModel(
    private val searchInteractor: SearchInteractor
) : ViewModel() {
    var currentQuery: String = ""

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


    private val handler = android.os.Handler(android.os.Looper.getMainLooper())
    private val debounceRunnable = Runnable { searchTracks(currentQuery) }

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
        handler.removeCallbacks(debounceRunnable)
        handler.postDelayed(debounceRunnable, 1000)
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
        handler.removeCallbacks(debounceRunnable)
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

    /* companion object {
         fun provideFactory(
             interactor: SearchInteractor
         ): ViewModelProvider.Factory =
             object : ViewModelProvider.Factory {
                 @Suppress("UNCHECKED_CAST")
                 override fun <T : ViewModel> create(modelClass: Class<T>): T {
                     return SearchViewModel(interactor) as T
                 }
             }
     }*/
}
