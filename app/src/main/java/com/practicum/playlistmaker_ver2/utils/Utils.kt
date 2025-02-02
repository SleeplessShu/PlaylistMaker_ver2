package com.practicum.playlistmaker_ver2.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.TypedValue
import com.practicum.playlistmaker_ver2.database.data.TrackEntity
import com.practicum.playlistmaker_ver2.playlist.data.database.entity.PlaylistTrackEntity
import java.io.Serializable

fun Context.formatDpToPx(dp: Int): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        this.resources.displayMetrics
    ).toInt()
}


inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(
        key,
        T::class.java
    )

    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T

}
fun TrackEntity.toTrackPlaylistEntity() = PlaylistTrackEntity(
    trackId.toLong(),
    trackName,
    country,
    releaseDate,
    collectionName,
    primaryGenreName,
    artistName,
    trackTime.toInt(),
    previewUrl,
    artworkUrl500,
    inFavorite,
    System.currentTimeMillis()
)