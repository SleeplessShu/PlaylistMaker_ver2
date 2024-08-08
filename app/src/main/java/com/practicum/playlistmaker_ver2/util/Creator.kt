package com.practicum.playlistmaker_ver2.util

import android.app.Activity
import android.content.Context
import android.os.Handler
import com.practicum.playlistmaker_ver2.data.dto.TracksRepositoryImpl
import com.practicum.playlistmaker_ver2.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker_ver2.data.repository.ClickedTracksRepositoryImpl
import com.practicum.playlistmaker_ver2.data.repository.NightModeRepositoryImpl
import com.practicum.playlistmaker_ver2.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker_ver2.databinding.ActivitySearchBinding
import com.practicum.playlistmaker_ver2.domain.api.TracksInteractor
import com.practicum.playlistmaker_ver2.domain.api.TracksRepository
import com.practicum.playlistmaker_ver2.domain.repository.ClickedTracksRepository
import com.practicum.playlistmaker_ver2.domain.repository.NightModeRepository
import com.practicum.playlistmaker_ver2.domain.use_case.AddClickedTrackUseCase
import com.practicum.playlistmaker_ver2.domain.use_case.EraseClickedTracksUseCase
import com.practicum.playlistmaker_ver2.domain.use_case.GetClickedTracksUseCase
import com.practicum.playlistmaker_ver2.domain.use_case.GetNightModeStatusUseCase
import com.practicum.playlistmaker_ver2.domain.use_case.ProvidePlayerControllerUseCase
import com.practicum.playlistmaker_ver2.domain.use_case.GetSearchControllerUseCase
import com.practicum.playlistmaker_ver2.domain.use_case.GetSharedPreferencesManagerUseCase
import com.practicum.playlistmaker_ver2.domain.use_case.GetTracksInteractorUseCase

import com.practicum.playlistmaker_ver2.domain.use_case.SetNightModeStatusUseCase
import com.practicum.playlistmaker_ver2.presentation.PlayerController


import com.practicum.playlistmaker_ver2.presentation.SearchController
import com.practicum.playlistmaker_ver2.ui.search.TrackAdapter

object Creator {

    // SEARCH ACTIVITY
    private fun provideTracksRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideTracksInteractor(context: Context): TracksInteractor {
        val tracksRepository = provideTracksRepository(context)
        return GetTracksInteractorUseCase(tracksRepository).execute(context)
    }

    fun provideTrackSearchController(
        activity: Activity,
        binding: ActivitySearchBinding
    ): SearchController {
        return GetSearchControllerUseCase().execute(activity, binding)
    }

    fun provideSharedPreferencesManager(context: Context): SharedPreferencesManager {
        return GetSharedPreferencesManagerUseCase().execute(context)
    }

    // PLAYER ACTIVITY
    fun providePlayerControllerUseCase(
        activity: Activity,
        binding: ActivityPlayerBinding,
        mainThreadHandler: Handler
    ): PlayerController {
        return ProvidePlayerControllerUseCase().execute(activity, binding, mainThreadHandler)
    }


    //SHARED PREFERENCES
    //NightMode
    private fun provideNightModeRepositoryUseCase(context: Context): NightModeRepository {
        return NightModeRepositoryImpl(context)
    }

    fun provideSetNightModeStatusUseCase(context: Context): SetNightModeStatusUseCase {
        return SetNightModeStatusUseCase(
            provideNightModeRepositoryUseCase(context)
        )
    }

    fun provideGetNightModeStatusUseCase(context: Context): GetNightModeStatusUseCase {
        return GetNightModeStatusUseCase(
            provideNightModeRepositoryUseCase(context)
        )
    }

    //Clicked tracks
    private fun provideClickedTracksRepository(context: Context): ClickedTracksRepository {
        return ClickedTracksRepositoryImpl(context)
    }

    fun provideGetClickedTracksUseCase(context: Context): GetClickedTracksUseCase {
        return GetClickedTracksUseCase(provideClickedTracksRepository(context))
    }

    fun provideAddClickedTracksUseCase(context: Context): AddClickedTrackUseCase {
        return AddClickedTrackUseCase(provideClickedTracksRepository(context))
    }

    fun provideEraseClickedTracksUseCase(context: Context): EraseClickedTracksUseCase {
        return EraseClickedTracksUseCase(provideClickedTracksRepository(context))
    }

}