package com.practicum.playlistmaker_ver2.di

import com.practicum.playlistmaker_ver2.favorite.domain.interactors.FavoriteInteractor
import com.practicum.playlistmaker_ver2.favorite.domain.repositories.FavoriteInteractorImpl
import com.practicum.playlistmaker_ver2.playlist.domain.interactors.PlaylistDatabaseInteractor
import com.practicum.playlistmaker_ver2.playlist.domain.interactors.PlaylistInteractor
import com.practicum.playlistmaker_ver2.playlist.domain.interactorsImpl.PlaylistDatabaseInteractorImpl
import com.practicum.playlistmaker_ver2.playlist.domain.interactorsImpl.PlaylistInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<FavoriteInteractor> {
        FavoriteInteractorImpl(get())
    }
    single<PlaylistDatabaseInteractor> {
        PlaylistDatabaseInteractorImpl(get())
    }
    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }
}