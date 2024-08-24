package com.practicum.playlistmaker_ver2.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.practicum.playlistmaker_ver2.domain.repository.ThemeStatusRepository


class ThemeStatusRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    ThemeStatusRepository {

    private companion object {
        const val THEME_STATUS_SHARED_PREFERENCES_KEY: String = "NightMode"

    }

    /*

        )
    */
    override fun setStatus(currentStatus: Boolean) {
        sharedPreferences.edit {
            putBoolean(THEME_STATUS_SHARED_PREFERENCES_KEY, currentStatus)
        }
    }

    override fun getStatus(): Boolean {

        return sharedPreferences.getBoolean(THEME_STATUS_SHARED_PREFERENCES_KEY, false)
    }
}
