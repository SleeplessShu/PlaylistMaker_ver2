package com.practicum.playlistmaker_ver2.di

import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker_ver2.player.ui.PlayerViewModel
import com.practicum.playlistmaker_ver2.search.ui.SearchViewModel
import com.practicum.playlistmaker_ver2.settings.ui.SettingsViewModel
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

    factory<Handler> {
        Handler(Looper.getMainLooper())
    }
}