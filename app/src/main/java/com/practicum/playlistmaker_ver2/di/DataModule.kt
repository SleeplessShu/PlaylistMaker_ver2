package com.practicum.playlistmaker_ver2.di


import android.content.Context
import com.google.gson.Gson
import com.practicum.playlistmaker_ver2.App
import com.practicum.playlistmaker_ver2.search.data.dto.TracksRepositoryImpl
import com.practicum.playlistmaker_ver2.search.data.network.ITunesApiService
import com.practicum.playlistmaker_ver2.search.data.network.NetworkClient
import com.practicum.playlistmaker_ver2.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker_ver2.search.data.repository.ClickedTracksRepositoryImpl
import com.practicum.playlistmaker_ver2.search.domain.api.TracksRepository
import com.practicum.playlistmaker_ver2.search.domain.repository.ClickedTracksRepository
import com.practicum.playlistmaker_ver2.settings.data.repositories.ExternalNavigatorRepositoryImpl
import com.practicum.playlistmaker_ver2.settings.data.repositories.SettingsRepositoryImpl
import com.practicum.playlistmaker_ver2.settings.data.repositories.SharingRepositoryImpl
import com.practicum.playlistmaker_ver2.settings.domain.repositories.ExternalNavigatorRepository
import com.practicum.playlistmaker_ver2.settings.domain.repositories.SettingsRepository
import com.practicum.playlistmaker_ver2.settings.domain.repositories.SharingRepository
import org.koin.core.qualifier.named


import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<ITunesApiService> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApiService::class.java)
    }

    single { (Gson()) }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), get())
    }

    single(named("previousSearchSharedPreferences")) {
        App.appContext
            .getSharedPreferences("previous_search_result", Context.MODE_PRIVATE)
    }

    single(named("themePreferences")) {
        App.appContext
            .getSharedPreferences("NightMode", Context.MODE_PRIVATE)
    }

    single<ClickedTracksRepository> {
        ClickedTracksRepositoryImpl(get(named("previousSearchSharedPreferences")), get())
    }
    single<SettingsRepository> {
        SettingsRepositoryImpl(get(named("themePreferences")))
    }

    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }

    single<SharingRepository> {
        SharingRepositoryImpl()
    }
    single<ExternalNavigatorRepository> {
        ExternalNavigatorRepositoryImpl()
    }
}