package com.practicum.playlistmaker_ver2.di

import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker_ver2.mediateka.presentation.FavoriteTracksViewModel
import com.practicum.playlistmaker_ver2.mediateka.presentation.MediatekaViewModel
import com.practicum.playlistmaker_ver2.mediateka.presentation.FavoritePlaylistsViewModel
import com.practicum.playlistmaker_ver2.player.ui.PlayerViewModel
import com.practicum.playlistmaker_ver2.search.presentation.SearchViewModel
import com.practicum.playlistmaker_ver2.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        PlayerViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        FavoriteTracksViewModel()
    }

    viewModel {
        MediatekaViewModel()
    }

    viewModel {
        FavoritePlaylistsViewModel()
    }



    factory<Handler> {
        Handler(Looper.getMainLooper())
    }
}