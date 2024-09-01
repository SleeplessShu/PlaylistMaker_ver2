package com.practicum.playlistmaker_ver2.settings.domain.repositories

interface SettingsRepository {
    fun getThemeSettings(): Boolean
    fun setThemeSetting(currentStatus: Boolean)

    fun switchTheme()
}