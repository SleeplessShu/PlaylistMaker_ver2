package com.practicum.playlistmaker_ver2.utils

import android.content.Context
import com.practicum.playlistmaker_ver2.R
import java.util.Locale

object PluralUtils {
    fun formatTrackCount(context: Context, count: Int): String {
        val localizedContext = context.createConfigurationContext(
            context.resources.configuration.apply {
                setLocale(Locale("ru"))
            }
        )
        return localizedContext.resources.getQuantityString(
            R.plurals.track_count, count, count
        )
    }
}