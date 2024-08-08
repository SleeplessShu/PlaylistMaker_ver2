package com.practicum.playlistmaker_ver2.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.practicum.playlistmaker_ver2.domain.repository.NightModeRepository


class NightModeRepositoryImpl(context: Context) : NightModeRepository {

    companion object {
        const val NIGHT_MODE_SHARED_PREFERENCES_KEY: String = "NightMode"
        const val NIGHT_MODE_SHARED_PREFERENCES_NAME: String = "Settings"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        NIGHT_MODE_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE
    )

    override fun setNightModeStatus(currentStatus: Boolean) {
        sharedPreferences.edit().putBoolean(NIGHT_MODE_SHARED_PREFERENCES_KEY, currentStatus)
            .apply()
    }

    override fun getNightModeStatus(): Boolean {
        return sharedPreferences.getBoolean(NIGHT_MODE_SHARED_PREFERENCES_KEY, false)
    }
}
