package com.practicum.playlistmaker_ver2.settings.domain.repositories

import com.practicum.playlistmaker_ver2.settings.domain.model.EmailData

interface SharingRepository {
    fun getShareAppLink(): String
    fun getSupportEmailData(): EmailData
    fun getTermsLink(): String
}