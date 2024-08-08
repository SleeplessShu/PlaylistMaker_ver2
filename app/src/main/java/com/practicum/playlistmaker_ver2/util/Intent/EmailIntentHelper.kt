package com.practicum.playlistmaker_ver2.util.Intent

import android.content.Intent
import android.net.Uri

object EmailIntentHelper {
    fun createSupportEmailIntent(email: String, subject: String, text: String): Intent {
        return Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, text)
        }
    }
}