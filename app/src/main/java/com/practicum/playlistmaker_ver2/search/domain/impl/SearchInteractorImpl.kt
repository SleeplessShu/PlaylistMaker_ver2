package com.practicum.playlistmaker_ver2.search.domain.impl

import com.practicum.playlistmaker_ver2.search.domain.interactor.TrackInteractor
import com.practicum.playlistmaker_ver2.search.domain.models.Track
import com.practicum.playlistmaker_ver2.search.domain.interactor.SearchInteractor
import com.practicum.playlistmaker_ver2.search.domain.interactor.ClickedTracksInteractor
import kotlinx.coroutines.flow.Flow

class SearchInteractorImpl(
    private val trackInteractor: TrackInteractor,
    private val clickedTracksInteractor: ClickedTracksInteractor
) : SearchInteractor {

    override fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>> {
        return trackInteractor.doRequest(expression)
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
