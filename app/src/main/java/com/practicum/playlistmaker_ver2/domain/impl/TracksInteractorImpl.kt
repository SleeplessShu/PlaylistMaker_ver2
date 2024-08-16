package com.practicum.playlistmaker_ver2.domain.impl


import com.practicum.playlistmaker_ver2.domain.api.TrackConsumer
import com.practicum.playlistmaker_ver2.data.network.NetworkClient
import com.practicum.playlistmaker_ver2.domain.api.TracksRepository
import com.practicum.playlistmaker_ver2.util.Resource
import java.util.concurrent.ExecutorService


class TracksInteractorImpl(
    private val repository: TracksRepository,
    private val executor: ExecutorService
) : NetworkClient {


    override fun doRequest(expression: String, consumer: TrackConsumer) {
        executor.execute {
            when (val resource = repository.searchTracks(expression)) {
                is Resource.Success -> consumer.consume(resource.data, null)
                is Resource.Error -> consumer.consume(null, resource.message)
            }
        }
    }
}
