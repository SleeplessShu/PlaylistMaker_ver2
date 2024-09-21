package com.practicum.playlistmaker_ver2.settings.data.repositories

import android.content.Context
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.settings.domain.model.EmailData
import com.practicum.playlistmaker_ver2.settings.domain.repositories.SharingRepository

class SharingRepositoryImpl(val context: Context) : SharingRepository {
    override fun getShareAppLink(): String {
        return context.getString(R.string.shareApp)
    }

    override fun getSupportEmailData(): EmailData {
        return EmailData(
            context.getString(R.string.supportEmail),
            context.getString(R.string.mailToSupportSubject),
            context.getString(R.string.mailToSupportText)
        )
    }

    override fun getTermsLink(): String {
        return context.getString(R.string.agreement)
    }
}