package com.practicum.playlistmaker_ver2.di

import com.practicum.playlistmaker_ver2.favorite.presentation.FavoriteViewModel
import com.practicum.playlistmaker_ver2.playlist.presentation.viewmodels.CreatePlaylistViewModel
import com.practicum.playlistmaker_ver2.playlist.presentation.viewmodels.PlaylistFragmentViewModel
import com.practicum.playlistmaker_ver2.playlist.presentation.viewmodels.PlaylistInfoViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module{
    viewModel{
        PlaylistFragmentViewModel()
    }
    
    viewModel {
        FavoriteViewModel(get(),androidContext())
    }
    viewModel {
        CreatePlaylistViewModel(get(),get(),get())
    }
    viewModel{
        PlaylistInfoViewModel(get(),get() )
    }
}