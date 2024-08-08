package com.practicum.playlistmaker_ver2.util

import android.content.Context
import android.content.SharedPreferences


class SharedPreferencesManager(context: Context) {
    companion object {
        const val CLICKED_TRACKS_REPOSITORY_NAME: String = "previous_search_result"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        CLICKED_TRACKS_REPOSITORY_NAME, Context.MODE_PRIVATE
    )

    fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    fun getSharedPreferences(): SharedPreferences {
        return sharedPreferences
    }
}
