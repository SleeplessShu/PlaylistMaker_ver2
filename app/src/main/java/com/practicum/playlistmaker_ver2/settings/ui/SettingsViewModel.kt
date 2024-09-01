package com.practicum.playlistmaker_ver2.settings.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker_ver2.creator.Creator


class SettingsViewModel : ViewModel() {
    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel()
            }
        }
    }

    private val sharingInteractor = Creator.provideSharingInteractor()
    private val settingsInteractor = Creator.provideSettingsInteractor()

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