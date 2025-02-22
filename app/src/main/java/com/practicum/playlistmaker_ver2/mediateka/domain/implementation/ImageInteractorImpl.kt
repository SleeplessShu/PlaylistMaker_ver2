package com.practicum.playlistmaker_ver2.mediateka.domain.implementation

import android.net.Uri
import com.practicum.playlistmaker_ver2.mediateka.domain.api.ImageInteractor
import com.practicum.playlistmaker_ver2.player.domain.repositories.ImageRepository

class ImageInteractorImpl(
    private val imageRepository: ImageRepository
) : ImageInteractor {
    override fun saveImageToPrivateStorage(uri: Uri): Uri? {
        return imageRepository.saveImageToPrivateStorage(uri)
    }
}