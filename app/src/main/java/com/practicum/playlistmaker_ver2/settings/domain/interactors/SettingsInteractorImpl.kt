package com.practicum.playlistmaker_ver2.settings.domain.interactors

import com.practicum.playlistmaker_ver2.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker_ver2.settings.domain.repositories.SettingsRepository

class SettingsInteractorImpl(private val settings: SettingsRepository) :
    SettingsInteractor {

    override fun getThemeSettings(): Boolean {
        return settings.getThemeSettings()
    }

    override fun setThemeSetting(currentStatus: Boolean) {
        settings.setThemeSetting(currentStatus)
    }

}