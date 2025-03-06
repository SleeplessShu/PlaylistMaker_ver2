package com.practicum.playlistmaker_ver2.mediateka.domain.api

import android.net.Uri

interface ImageInteractor {
    fun saveImageToPrivateStorage(uri: Uri): Uri?
}