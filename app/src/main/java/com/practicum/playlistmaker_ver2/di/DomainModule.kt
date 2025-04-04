package com.practicum.playlistmaker_ver2.di

import com.practicum.playlistmaker_ver2.player.domain.api.LikedTracksInteractor
import com.practicum.playlistmaker_ver2.player.domain.impl.LikedTracksInteractorImpl
import com.practicum.playlistmaker_ver2.mediateka.domain.api.ImageInteractor
import com.practicum.playlistmaker_ver2.mediateka.domain.implementation.ImageInteractorImpl
import com.practicum.playlistmaker_ver2.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker_ver2.player.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker_ver2.playlist_editor.domain.interactor.PlaylistInteractor
import com.practicum.playlistmaker_ver2.playlist_editor.domain.implimentation.PlaylistInteractorImpl
import com.practicum.playlistmaker_ver2.search.domain.interactor.SearchInteractor
import com.practicum.playlistmaker_ver2.search.domain.interactor.TrackInteractor
import com.practicum.playlistmaker_ver2.search.domain.impl.ClickedTracksInteractorImpl
import com.practicum.playlistmaker_ver2.search.domain.impl.SearchInteractorImpl
import com.practicum.playlistmaker_ver2.search.domain.impl.TrackInteractorImpl
import com.practicum.playlistmaker_ver2.search.domain.interactor.ClickedTracksInteractor
import com.practicum.playlistmaker_ver2.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker_ver2.settings.domain.api.SharingInteractor
import com.practicum.playlistmaker_ver2.settings.domain.interactors.SettingsInteractorImpl
import com.practicum.playlistmaker_ver2.settings.domain.interactors.SharingInteractorImpl
import org.koin.dsl.module

val domainModule = module {
    single<ClickedTracksInteractor> {
        ClickedTracksInteractorImpl(get())
    }

    single<TrackInteractor> {
        TrackInteractorImpl(get())
    }

    single<SearchInteractor> {
        SearchInteractorImpl(get(), get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get(), get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    factory<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }

    single<LikedTracksInteractor> {
        LikedTracksInteractorImpl(get())
    }
    single<ImageInteractor> {
        ImageInteractorImpl(get())
    }
    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }
}