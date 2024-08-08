package com.practicum.playlistmaker_ver2.domain.impl


import android.util.Log
import com.practicum.playlistmaker_ver2.domain.api.TracksInteractor
import com.practicum.playlistmaker_ver2.domain.api.TracksRepository
import com.practicum.playlistmaker_ver2.util.Resource
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun searchTrack(expression: String, consumer: TracksInteractor.TrackConsumer) {
        executor.execute {
            Log.d("shu", "TracksInteractorImpl")
            when (val resource = repository.searchTracks(expression)) {
                is Resource.Success -> {
                    Log.d("shu", "TracksInteractorImpl -> Success")
                    consumer.consume(resource.data, null)
                }

                is Resource.Error -> {
                    Log.d("shu", "Error")
                    consumer.consume(null, resource.message)
                }
            }
        }
    }
}