package com.practicum.playlistmaker_ver2.creator

import android.content.Context
import com.practicum.playlistmaker_ver2.data.repository.NightModeRepositoryInSharedPreferencesImpl
import com.practicum.playlistmaker_ver2.data.repository.TracksRepositoryInSharedPreferencesImpl
import com.practicum.playlistmaker_ver2.domain.repository.NightModeRepositoryInSharedPreferences
import com.practicum.playlistmaker_ver2.domain.repository.TracksRepositoryInSharedPreferences
import com.practicum.playlistmaker_ver2.domain.use_case.GetNightModeStatusFromSharedPreferencesUseCase
import com.practicum.playlistmaker_ver2.domain.use_case.SetNightModeStatusInSharedPreferencesUseCase

object Creator {

    //SHARED PREFERENCES

    //NightMode
    private fun provideNightModeRepositoryInSharedPreferences(context: Context): NightModeRepositoryInSharedPreferences {
        return NightModeRepositoryInSharedPreferencesImpl(context = context)
    }

    private fun provideSetNightModeStatusInSharedPreferencesUseCase(context: Context): SetNightModeStatusInSharedPreferencesUseCase {
        return SetNightModeStatusInSharedPreferencesUseCase(
            provideNightModeRepositoryInSharedPreferences(context)
        )
    }

    private fun provideGetNightModeStatusInSharedPreferencesUseCase(context: Context): GetNightModeStatusFromSharedPreferencesUseCase {
        return GetNightModeStatusFromSharedPreferencesUseCase(
            provideNightModeRepositoryInSharedPreferences(context)
        )
    }


    //Clicked tracks
    private fun provideTracksRepositoryInSharedPreferences(context: Context): TracksRepositoryInSharedPreferences {
        return TracksRepositoryInSharedPreferencesImpl(context = context)
    }


}