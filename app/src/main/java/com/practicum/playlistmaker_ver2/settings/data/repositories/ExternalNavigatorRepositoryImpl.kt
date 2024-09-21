package com.practicum.playlistmaker_ver2.settings.data.repositories

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker_ver2.settings.domain.model.EmailData
import com.practicum.playlistmaker_ver2.settings.domain.repositories.ExternalNavigatorRepository

class ExternalNavigatorRepositoryImpl(val context: Context) : ExternalNavigatorRepository {
    override fun shareLink(link: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, link)
        context.startActivity(shareIntent)
    }

    override fun openLink(agreementUrl: String) {
        val agreementIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse(agreementUrl))
        context.startActivity(agreementIntent)
    }

    override fun openEmail(email: EmailData) {
        val supportIntent = Intent(Intent.ACTION_SENDTO)
        supportIntent.data = Uri.parse("mailto:")
        supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email.email))
        supportIntent.putExtra(Intent.EXTRA_SUBJECT, email.themeEmail)
        supportIntent.putExtra(Intent.EXTRA_TEXT, email.messageEmail)
        context.startActivity(supportIntent)
    }
}