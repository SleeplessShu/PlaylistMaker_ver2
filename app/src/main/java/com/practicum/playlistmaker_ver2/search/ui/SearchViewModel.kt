package com.practicum.playlistmaker_ver2.search.ui


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker_ver2.search.domain.api.SearchInteractor
import com.practicum.playlistmaker_ver2.search.domain.models.Track
import com.practicum.playlistmaker_ver2.search.ui.adapters.TrackAdapter

class SearchViewModel(
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> get() = _tracks

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _viewType = MutableLiveData<Int>()
    val viewType: LiveData<Int> get() = _viewType

    private val _isHistoryVisible = MutableLiveData<Boolean>()
    val isHistoryVisible: LiveData<Boolean> get() = _isHistoryVisible

    var currentQuery: String = ""
    private val handler = android.os.Handler(android.os.Looper.getMainLooper())
    private val debounceRunnable = Runnable { searchTracks() }

    fun onSearchQueryChanged(query: String) {
        currentQuery = query
    }

    fun searchTracks() {
        if (currentQuery.isEmpty()) {
            val history = searchInteractor.getSearchHistory()
            _tracks.value = history
            _isHistoryVisible.value = history.isNotEmpty()
            _viewType.value =
                if (history.isEmpty()) TrackAdapter.VIEW_TYPE_EMPTY else TrackAdapter.VIEW_TYPE_ITEM
            return
        }

        _isLoading.value = true
        searchInteractor.searchTracks(currentQuery) { foundTracks, errorMessage ->
            _isLoading.postValue(false)
            if (foundTracks != null) {
                when {
                    foundTracks.isNotEmpty() -> {
                        _tracks.postValue(foundTracks)
                        _viewType.postValue(TrackAdapter.VIEW_TYPE_ITEM)
                    }

                    foundTracks.isEmpty() -> {
                        _tracks.postValue(emptyList())
                        _viewType.postValue(TrackAdapter.VIEW_TYPE_NOTHING_FOUND)
                    }
                }
            } else {
                _tracks.postValue(emptyList())
                _errorMessage.postValue("${errorMessage}")
                _viewType.postValue(TrackAdapter.VIEW_TYPE_NO_INTERNET)
            }
        }
    }

    fun searchDebounce() {
        handler.removeCallbacks(debounceRunnable)
        handler.postDelayed(debounceRunnable, 1000)
    }

    fun clearSearchHistory() {
        searchInteractor.clearSearchHistory()
        _viewType.postValue(TrackAdapter.VIEW_TYPE_EMPTY)
        _tracks.postValue(emptyList())
        _isHistoryVisible.postValue(false)

    }

    fun addToSearchHistory(track: Track) {
        searchInteractor.addTrackToHistory(track)
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

    companion object {
        fun provideFactory(interactor: SearchInteractor): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(interactor) as T
                }
            }
    }
}
