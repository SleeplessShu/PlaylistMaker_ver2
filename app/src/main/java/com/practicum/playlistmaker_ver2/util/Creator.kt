package com.practicum.playlistmaker_ver2.util

import PlayerController
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Handler

import com.google.gson.Gson
import com.practicum.playlistmaker_ver2.data.dto.TracksRepositoryImpl

import com.practicum.playlistmaker_ver2.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker_ver2.data.repository.ClickedTracksRepositoryImpl
import com.practicum.playlistmaker_ver2.data.repository.ThemeStatusRepositoryImpl
import com.practicum.playlistmaker_ver2.domain.api.TrackInteractor

import com.practicum.playlistmaker_ver2.domain.api.TracksRepository
import com.practicum.playlistmaker_ver2.domain.impl.ClickedTracksInteractorImpl
import com.practicum.playlistmaker_ver2.domain.impl.ThemeStatusInteractorImpl
import com.practicum.playlistmaker_ver2.domain.impl.TrackInteractorImpl
import com.practicum.playlistmaker_ver2.domain.interactor.ClickedTracksInteractor
import com.practicum.playlistmaker_ver2.domain.interactor.ThemeStatusInteractor
import com.practicum.playlistmaker_ver2.domain.repository.ClickedTracksRepository
import com.practicum.playlistmaker_ver2.domain.repository.ThemeStatusRepository
import com.practicum.playlistmaker_ver2.presentation.mapper.TrackToPlayerTrackMapper
import java.util.concurrent.ExecutorService


object Creator {

    // SEARCH ACTIVITY
    private fun provideTracksRepository(connectivityManager: ConnectivityManager): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(connectivityManager))
    }

    fun provideTracksInteractor(
        connectivityManager: ConnectivityManager,
        executor: ExecutorService
    ): TrackInteractor {
        val tracksRepository = provideTracksRepository(connectivityManager)
        return TrackInteractorImpl(tracksRepository, executor)
    }


    // PLAYER ACTIVITY
    fun providePlayerController(
        mainThreadHandler: Handler,
        playerListener: PlayerController.PlayerListener,
        trackToPlayerTrackMapper: TrackToPlayerTrackMapper
    ): PlayerController {
        return PlayerController(
            mainThreadHandler = mainThreadHandler,
            playerListener = playerListener,
            trackToPlayerTrackMapper = trackToPlayerTrackMapper
        )
    }


    //SHARED PREFERENCES

    fun provideSharedPreferencesManager(appContext: Context): SharedPreferencesManager {
        return SharedPreferencesManager(appContext)
    }

    fun provideThemeStatusInteractor(sharedPreferences: SharedPreferences): ThemeStatusInteractor {
        return ThemeStatusInteractorImpl(provideThemeStatusRepository(sharedPreferences))
    }

    private fun provideThemeStatusRepository(sharedPreferences: SharedPreferences): ThemeStatusRepository {
        return ThemeStatusRepositoryImpl(sharedPreferences)
    }


    fun provideClickedTracksInteractor(
        sharedPreferences: SharedPreferences,
        gson: Gson
    ): ClickedTracksInteractor {
        return ClickedTracksInteractorImpl(provideClickedTracksRepository(sharedPreferences, gson))
    }

    private fun provideClickedTracksRepository(
        sharedPreferences: SharedPreferences,
        gson: Gson
    ): ClickedTracksRepository {
        return ClickedTracksRepositoryImpl(sharedPreferences, gson)
    }


}