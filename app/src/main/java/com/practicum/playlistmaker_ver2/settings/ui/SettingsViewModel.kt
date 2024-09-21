package com.practicum.playlistmaker_ver2.settings.ui


import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker_ver2.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker_ver2.settings.domain.api.SharingInteractor


class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {




    fun getTheme(): Boolean {
        return settingsInteractor.getThemeSettings()
    }

    fun setTheme(isChecked: Boolean) {
        settingsInteractor.setThemeSetting(isChecked)
    }

    fun supportSend() {
        sharingInteractor.openSupport()
    }

    fun openTerm() {
        sharingInteractor.openTerms()
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }
}