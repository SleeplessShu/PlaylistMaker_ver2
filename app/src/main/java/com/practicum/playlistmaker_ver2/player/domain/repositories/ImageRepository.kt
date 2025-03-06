package com.practicum.playlistmaker_ver2.player.domain.repositories

import android.net.Uri

interface ImageRepository {
    fun saveImageToPrivateStorage(uri: Uri): Uri?
}