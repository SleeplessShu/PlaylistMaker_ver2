package com.practicum.playlistmaker_ver2.search.domain.impl

import com.practicum.playlistmaker_ver2.search.domain.api.TrackInteractor
import com.practicum.playlistmaker_ver2.search.domain.models.Track
import com.practicum.playlistmaker_ver2.search.domain.api.SearchInteractor
import com.practicum.playlistmaker_ver2.search.domain.interactor.ClickedTracksInteractor

class SearchInteractorImpl(
    private val trackInteractor: TrackInteractor,
    private val clickedTracksInteractor: ClickedTracksInteractor
) : SearchInteractor {

    override fun searchTracks(expression: String, callback: (List<Track>?, String?) -> Unit) {
        trackInteractor.doRequest(expression) { foundTracks, errorMessage ->
            callback(foundTracks, errorMessage)
        }
    }

    override fun addTrackToHistory(track: Track) {
        clickedTracksInteractor.addTrack(track)
    }

    override fun getSearchHistory(): List<Track> {
        return clickedTracksInteractor.getTracks()
    }

    override fun clearSearchHistory() {
        clickedTracksInteractor.eraseTracks()
    }
}
