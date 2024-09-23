package com.practicum.playlistmaker_ver2.settings.ui


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker_ver2.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker_ver2.settings.domain.api.SharingInteractor
import com.practicum.playlistmaker_ver2.settings.models.ThemeViewState


class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {
    private var state = MutableLiveData(ThemeViewState())
    fun getThemeState(): LiveData<ThemeViewState> = state

    fun initializeTheme() {
        val isNightModeOn = settingsInteractor.getThemeSettings()
        state.postValue(ThemeViewState(isNightModeOn = isNightModeOn))
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