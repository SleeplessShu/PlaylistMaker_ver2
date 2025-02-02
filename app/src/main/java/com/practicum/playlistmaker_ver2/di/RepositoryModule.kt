package com.practicum.playlistmaker_ver2.di

import com.practicum.playlistmaker_ver2.favorite.data.repository.FavoriteRepositoryImpl
import com.practicum.playlistmaker_ver2.favorite.domain.repositories.FavoriteRepository
import com.practicum.playlistmaker_ver2.playlist.data.database.repository.PlaylistDatabaseRepositoryImpl
import com.practicum.playlistmaker_ver2.playlist.data.repository.PlaylistRepositoryImpl
import com.practicum.playlistmaker_ver2.playlist.domain.PlaylistDatabaseRepository
import com.practicum.playlistmaker_ver2.playlist.domain.repository.PlaylistRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<FavoriteRepository> {
        FavoriteRepositoryImpl(get(),get())
    }

    single<PlaylistDatabaseRepository> {
        PlaylistDatabaseRepositoryImpl(get())
    }
    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get())
    }
}