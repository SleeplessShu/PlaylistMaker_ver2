package com.practicum.playlistmaker_ver2.util.Intent

import android.content.Context
import android.content.Intent
import android.net.Uri

object AgreementIntentHelper {
    fun createAgreementWebIntent(context: Context, link: String): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse(link))
    }
}