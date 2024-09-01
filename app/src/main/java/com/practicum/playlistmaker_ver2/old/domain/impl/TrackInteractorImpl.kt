package com.practicum.playlistmaker_ver2.old.domain.impl


import com.practicum.playlistmaker_ver2.old.domain.api.TrackConsumer
import com.practicum.playlistmaker_ver2.old.domain.api.TrackInteractor
import com.practicum.playlistmaker_ver2.old.domain.api.TracksRepository
import com.practicum.playlistmaker_ver2.old.domain.models.Resource
import java.util.concurrent.ExecutorService


class TrackInteractorImpl(
    private val repository: TracksRepository,
    private val executor: ExecutorService
) : TrackInteractor {


    override fun doRequest(expression: String, consumer: TrackConsumer) {
        executor.execute {
            when (val resource = repository.searchTracks(expression)) {
                is Resource.Success -> consumer.consume(resource.data, null)
                is Resource.Error -> consumer.consume(null, resource.message)
            }
        }
    }
}
