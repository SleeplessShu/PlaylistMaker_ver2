package com.practicum.playlistmaker_ver2.domain.impl


import com.practicum.playlistmaker_ver2.domain.interactor.ThemeStatusInteractor
import com.practicum.playlistmaker_ver2.domain.repository.ThemeStatusRepository

class ThemeStatusInteractorImpl(
    private val themeStatusRepository: ThemeStatusRepository
) : ThemeStatusInteractor {
    override
    fun setThemeStatus(newStatus: Boolean) {
        themeStatusRepository.setStatus(newStatus)
    }

    override
    fun getThemeStatus(): Boolean {
        return themeStatusRepository.getStatus()
    }
}