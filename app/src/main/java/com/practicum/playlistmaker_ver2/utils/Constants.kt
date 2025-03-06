package com.practicum.playlistmaker_ver2.utils

import android.net.Uri
import com.practicum.playlistmaker_ver2.R.drawable

object Constants {
    val PLACEHOLDER_URI: Uri = Uri.parse("android.resource://com.practicum.playlistmaker_ver2/${drawable.placeholder}")
    val DEFAULT_PLAYLIST_IMAGE_URI: Uri = Uri.parse("android.resource://com.practicum.playlistmaker_ver2/${drawable.playlist_default_image}")
}