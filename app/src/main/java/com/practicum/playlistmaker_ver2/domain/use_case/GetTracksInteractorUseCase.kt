package com.practicum.playlistmaker_ver2.domain.use_case

import android.content.Context
import com.practicum.playlistmaker_ver2.domain.api.TracksInteractor
import com.practicum.playlistmaker_ver2.domain.api.TracksRepository
import com.practicum.playlistmaker_ver2.domain.impl.TracksInteractorImpl

class GetTracksInteractorUseCase(private val tracksRepository: TracksRepository) {
    fun execute(context: Context): TracksInteractor {
        return TracksInteractorImpl(tracksRepository)
    }
}