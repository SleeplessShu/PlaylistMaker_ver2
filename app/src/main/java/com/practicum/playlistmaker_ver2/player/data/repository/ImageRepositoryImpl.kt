package com.practicum.playlistmaker_ver2.player.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.net.toUri
import com.practicum.playlistmaker_ver2.player.domain.repositories.ImageRepository
import java.io.File
import java.io.FileOutputStream


class ImageRepositoryImpl(
    private val context: Context
) : ImageRepository {
    override fun saveImageToPrivateStorage(uri: Uri): Uri? {

        val contentResolver = context.contentResolver

        try {
            val fileName = "playlist_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, fileName)

            contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            return file.toUri()

        } catch (e: Exception) {
            Toast.makeText(context, "Ошибка сохранения изображения", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
            return null
        }
    }
}
