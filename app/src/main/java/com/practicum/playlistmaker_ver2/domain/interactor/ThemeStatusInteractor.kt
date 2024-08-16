package com.practicum.playlistmaker_ver2.domain.interactor


interface ThemeStatusInteractor {
    fun setThemeStatus(newStatus: Boolean)

    fun getThemeStatus(): Boolean

}