package com.practicum.playlistmaker_ver2.creator

import android.content.Context
import com.practicum.playlistmaker_ver2.data.repository.NightModeRepositoryImpl
import com.practicum.playlistmaker_ver2.domain.repository.NightModeRepository
import com.practicum.playlistmaker_ver2.domain.use_case.GetNightModeStatusUseCase
import com.practicum.playlistmaker_ver2.domain.use_case.SetNightModeStatusUseCase

object Creator {

    //SHARED PREFERENCES

    //NightMode
    private fun provideNightModeRepositoryUseCase(context: Context): NightModeRepository {
        return NightModeRepositoryImpl(context = context)
    }

    fun provideSetNightModeStatusUseCase(context: Context): SetNightModeStatusUseCase {
        return SetNightModeStatusUseCase(
            provideNightModeRepositoryUseCase(context)
        )
    }

    fun provideGetNightModeStatusUseCase(context: Context): GetNightModeStatusUseCase {
        return GetNightModeStatusUseCase(
            provideNightModeRepositoryUseCase(context)
        )
    }
    /*

        //Clicked tracks
        private fun provideTracksRepositoryInSharedPreferences(context: Context): TracksRepositoryInSharedPreferences {
            return TracksRepositoryInSharedPreferencesImpl(context = context)
        }

    */
}