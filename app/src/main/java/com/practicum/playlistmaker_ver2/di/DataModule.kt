package com.practicum.playlistmaker_ver2.di


import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import androidx.room.Room
import com.google.gson.Gson
import com.practicum.playlistmaker_ver2.App
import com.practicum.playlistmaker_ver2.player.data.impl.LikedTracksRepositoryImpl
import com.practicum.playlistmaker_ver2.player.data.converters.TrackDbConverter
import com.practicum.playlistmaker_ver2.player.data.LikedTracksDatabase
import com.practicum.playlistmaker_ver2.player.domain.repositories.LikedTracksRepository
import com.practicum.playlistmaker_ver2.playlist.data.repositories.PlaylistDatabase
import com.practicum.playlistmaker_ver2.player.data.impl.ImageRepositoryImpl
import com.practicum.playlistmaker_ver2.player.data.impl.PlayerRepositoryImpl
import com.practicum.playlistmaker_ver2.player.domain.repositories.ImageRepository
import com.practicum.playlistmaker_ver2.player.domain.repositories.PlayerRepository
import com.practicum.playlistmaker_ver2.playlist.data.repositories.PlaylistRepositoryImpl
import com.practicum.playlistmaker_ver2.playlist.domain.PlaylistRepository
import com.practicum.playlistmaker_ver2.playlist.data.repositories.TracksInPlaylistsDatabase

import com.practicum.playlistmaker_ver2.search.data.dto.TracksRepositoryImpl
import com.practicum.playlistmaker_ver2.search.data.network.ITunesApiService
import com.practicum.playlistmaker_ver2.search.data.network.NetworkClient
import com.practicum.playlistmaker_ver2.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker_ver2.search.data.repository.ClickedTracksRepositoryImpl
import com.practicum.playlistmaker_ver2.search.domain.repository.TracksRepository
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

    factory { (Gson()) }

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
        SharingRepositoryImpl(get())
    }
    single<ExternalNavigatorRepository> {
        ExternalNavigatorRepositoryImpl(get())
    }

    single {
        Room.databaseBuilder(get(), LikedTracksDatabase::class.java, "likedTracksDatabase.db")
            .build()
    }

    single {
        Room.databaseBuilder(get(), PlaylistDatabase::class.java, "playlistDatabase.db")
            .build()
    }

    single {
        Room.databaseBuilder(get(), TracksInPlaylistsDatabase::class.java, "tracksInPlaylists.db")
            .build()
    }

    single { get<TracksInPlaylistsDatabase>().getTracksInPlaylistsDao() }

    single { get<PlaylistDatabase>().getPlaylistDao() }

    factory { TrackDbConverter() }

    factory<PlayerRepository> {
        PlayerRepositoryImpl(get(), get())
    }

    single<LikedTracksRepository> {
        LikedTracksRepositoryImpl(get(), get())
    }

    single<ImageRepository> {
        ImageRepositoryImpl(get())
    }

    single<PlaylistRepository>{
        PlaylistRepositoryImpl(get(), get())
    }

    factory<Handler> {
        Handler()
    }

    factory<MediaPlayer> {
        MediaPlayer()
    }

    single<Context> { App.appContext }

}