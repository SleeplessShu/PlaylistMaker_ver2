package com.practicum.playlistmaker_ver2.settings.domain.repositories

import com.practicum.playlistmaker_ver2.settings.domain.model.EmailData

interface ExternalNavigatorRepository {
    fun shareLink(text: String)
    fun openLink(link: String)
    fun openEmail(email: EmailData)
}