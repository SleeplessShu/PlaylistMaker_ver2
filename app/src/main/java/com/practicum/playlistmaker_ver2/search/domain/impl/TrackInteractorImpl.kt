package com.practicum.playlistmaker_ver2.search.domain.impl


import com.practicum.playlistmaker_ver2.search.domain.api.TrackConsumer
import com.practicum.playlistmaker_ver2.search.domain.interactor.TrackInteractor
import com.practicum.playlistmaker_ver2.search.domain.repository.TracksRepository
import com.practicum.playlistmaker_ver2.search.domain.models.Resource
import java.util.concurrent.Executors


class TrackInteractorImpl(
    private val repository: TracksRepository
) : TrackInteractor {

    private val executor = Executors.newCachedThreadPool()
    override fun doRequest(expression: String, consumer: TrackConsumer) {
        executor.execute {
            when (val resource = repository.searchTracks(expression)) {
                is Resource.Success -> consumer.consume(resource.data, null)
                is Resource.Error -> consumer.consume(null, resource.message)
            }
        }
    }
}
