package com.practicum.playlistmaker_ver2.util.Intent

import android.content.Context
import android.content.Intent

object ShareIntentHelper {

    fun createShareAppIntent(context: Context, shareText: String): Intent {
        return Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)

        }
    }
}