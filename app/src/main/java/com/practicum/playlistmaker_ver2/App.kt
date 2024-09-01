package com.practicum.playlistmaker_ver2

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker_ver2.creator.Creator

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)
        val settingsInteractor = Creator.provideSettingsInteractor()
        val isNightModeOn = settingsInteractor.getThemeSettings()
        AppCompatDelegate.setDefaultNightMode(
            if (isNightModeOn) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}