package com.practicum.playlistmaker_ver2.creator


import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.google.gson.Gson
import com.practicum.playlistmaker_ver2.old.data.dto.TracksRepositoryImpl
import com.practicum.playlistmaker_ver2.old.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker_ver2.old.data.repository.ClickedTracksRepositoryImpl
import com.practicum.playlistmaker_ver2.old.domain.api.TrackInteractor
import com.practicum.playlistmaker_ver2.old.domain.api.TracksRepository
import com.practicum.playlistmaker_ver2.old.domain.impl.ClickedTracksInteractorImpl
import com.practicum.playlistmaker_ver2.old.domain.impl.TrackInteractorImpl
import com.practicum.playlistmaker_ver2.old.domain.interactor.ClickedTracksInteractor
import com.practicum.playlistmaker_ver2.old.domain.repository.ClickedTracksRepository
import com.practicum.playlistmaker_ver2.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker_ver2.player.domain.interactors.PlayerInteractorImpl
import com.practicum.playlistmaker_ver2.util.SharedPreferencesManager
import com.practicum.playlistmaker_ver2.settings.data.repositories.ExternalNavigatorRepositoryImpl
import com.practicum.playlistmaker_ver2.settings.data.repositories.SettingsRepositoryImpl
import com.practicum.playlistmaker_ver2.settings.data.repositories.SharingRepositoryImpl
import com.practicum.playlistmaker_ver2.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker_ver2.settings.domain.api.SharingInteractor
import com.practicum.playlistmaker_ver2.settings.domain.interactors.SettingsInteractorImpl
import com.practicum.playlistmaker_ver2.settings.domain.interactors.SharingInteractorImpl
import com.practicum.playlistmaker_ver2.settings.domain.repositories.ExternalNavigatorRepository
import com.practicum.playlistmaker_ver2.settings.domain.repositories.SettingsRepository
import com.practicum.playlistmaker_ver2.settings.domain.repositories.SharingRepository
import java.util.concurrent.ExecutorService


object Creator {
    private lateinit var application: Application
    fun initApplication(application: Application) {
        Creator.application = application
    }

    // SEARCH ACTIVITY
    private fun provideTracksRepository(connectivityManager: ConnectivityManager): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(connectivityManager))
    }

    fun provideTracksInteractor(
        connectivityManager: ConnectivityManager, executor: ExecutorService
    ): TrackInteractor {
        val tracksRepository = provideTracksRepository(connectivityManager)
        return TrackInteractorImpl(tracksRepository, executor)
    }


    // PLAYER ACTIVITY
    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl()

    }


    //SHARED PREFERENCES

    fun provideSharedPreferencesManager(appContext: Context): SharedPreferencesManager {
        return SharedPreferencesManager(appContext)
    }

    fun provideSharedPreferences(keyPref: String): SharedPreferences {
        return application.getSharedPreferences(keyPref, Context.MODE_PRIVATE)
    }


    fun provideClickedTracksInteractor(
        sharedPreferences: SharedPreferences, gson: Gson
    ): ClickedTracksInteractor {
        return ClickedTracksInteractorImpl(provideClickedTracksRepository(sharedPreferences, gson))
    }

    private fun provideClickedTracksRepository(
        sharedPreferences: SharedPreferences, gson: Gson
    ): ClickedTracksRepository {
        return ClickedTracksRepositoryImpl(sharedPreferences, gson)
    }


    //THEME STATUS
    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(provideSettingsRepository())
    }

    private fun provideSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl()
    }

    //SHARING
    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(provideSharingRepository(), provideExternalNavigator())
    }

    private fun provideSharingRepository(): SharingRepository {
        return SharingRepositoryImpl(application)
    }

    private fun provideExternalNavigator(): ExternalNavigatorRepository {
        return ExternalNavigatorRepositoryImpl(application)
    }

}