package com.practicum.playlistmaker_ver2.settings.domain.api

interface SharingInteractor {
    fun shareApp()
    fun openTerms()
    fun openSupport()

    fun sharePlaylist(playlistData: String)
}