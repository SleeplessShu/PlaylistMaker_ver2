package com.practicum.playlistmaker_ver2.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker_ver2.domain.repository.NightModeRepositoryInSharedPreferences
import com.practicum.playlistmaker_ver2.ui.settings.ActivitySettings

class NightModeRepositoryInSharedPreferencesImpl(context: Context) :
    NightModeRepositoryInSharedPreferences {

    companion object {
        const val NIGHT_MODE_SHARED_PREFERENCES_KEY: String = "NightMode"
        const val NIGHT_MODE_SHARED_PREFERENCES_NAME: String = "Settings"

    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        NIGHT_MODE_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE
    )

    override fun setNightModeStatusInSharedPreferencesUseCase(currentStatus: Boolean) {
        sharedPreferences.edit().putBoolean(NIGHT_MODE_SHARED_PREFERENCES_KEY, currentStatus)
            .apply()
    }

    override fun getNightModeStatusInSharedPreferencesUseCase(): Boolean {
        return sharedPreferences.getBoolean(NIGHT_MODE_SHARED_PREFERENCES_KEY, false)
    }
}