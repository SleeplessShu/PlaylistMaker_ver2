package com.practicum.playlistmaker_ver2

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.Room
import com.practicum.playlistmaker_ver2.database.data.LikedTracksDatabase
import com.practicum.playlistmaker_ver2.di.dataModule
import com.practicum.playlistmaker_ver2.di.domainModule
import com.practicum.playlistmaker_ver2.di.interactorModule
import com.practicum.playlistmaker_ver2.di.presentationModule
import com.practicum.playlistmaker_ver2.di.repositoryModule
import com.practicum.playlistmaker_ver2.di.viewModelModule
import com.practicum.playlistmaker_ver2.settings.domain.api.SettingsInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin


class App : Application() {
    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext

        startKoin {
            androidContext(this@App)
            modules(dataModule, domainModule, presentationModule, interactorModule, repositoryModule, viewModelModule)
        }

        val settingsInteractor: SettingsInteractor = getKoin().get()
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