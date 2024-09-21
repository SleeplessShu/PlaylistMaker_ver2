package com.practicum.playlistmaker_ver2

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker_ver2.creator.Creator

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)
        appContext = applicationContext
        val settingsInteractor = Creator.provideSettingsInteractor()
        val isNightModeOn = settingsInteractor.getThemeSettings()
        AppCompatDelegate.setDefaultNightMode(
            if (isNightModeOn) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    companion object {
        lateinit var appContext: Context
            private set
    }
}