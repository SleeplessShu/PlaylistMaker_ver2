package com.practicum.playlistmaker_ver2

import android.content.Context
import android.util.TypedValue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDpToPx(context: Context, dp: Int): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        context.resources.displayMetrics
    ).toInt()
}

fun formatMillisToMinutesSeconds(trackTimeMillis: Long): String {
    val minutes = trackTimeMillis / 1000 / 60
    val seconds = (trackTimeMillis / 1000 % 60)
    return String.format("%02d:%02d", minutes, seconds)
}

fun getHiResCoverArtwork(link: String): String {
    return link.replaceAfterLast('/', "512x512bb.jpg")
}

fun formatLnToStr(long: Long): String {
    val date = Date(long)
    val format = SimpleDateFormat("mm:ss", Locale.getDefault())
    return format.format(date)
}
