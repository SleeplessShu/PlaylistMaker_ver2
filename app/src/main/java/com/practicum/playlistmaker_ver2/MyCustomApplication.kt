package com.practicum.playlistmaker_ver2

import android.app.Application
import android.content.res.Configuration

class MyCustomApplication : Application() {
    // Вызывается при старте приложения, до того как инициализируются другие объекты
    override fun onCreate() {
        super.onCreate()
    }

    // Вызывается, когда конфигурация телефона изменена
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    // Вызывается, когда заканчивается память в системе
    override fun onLowMemory() {
        super.onLowMemory()
    }
}