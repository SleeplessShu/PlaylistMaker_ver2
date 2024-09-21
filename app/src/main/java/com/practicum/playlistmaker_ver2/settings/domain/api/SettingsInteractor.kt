package com.practicum.playlistmaker_ver2.settings.domain.api

interface SettingsInteractor {
    fun getThemeSettings(): Boolean
    fun setThemeSetting(currentStatus: Boolean)
}
