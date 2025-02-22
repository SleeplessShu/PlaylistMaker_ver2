package com.practicum.playlistmaker_ver2.playlist.data.entities

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "name", typeAffinity = ColumnInfo.TEXT)
    val name: String,

    @ColumnInfo(name = "description", typeAffinity = ColumnInfo.TEXT)
    val description: String = "",

    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.TEXT)
    val image: String = "",

    @ColumnInfo(name = "tracksIDsList", typeAffinity = ColumnInfo.TEXT)
    val tracksIDsList: String = "",

    @ColumnInfo(name = "tracksCount", typeAffinity = ColumnInfo.INTEGER)
    val tracksCount: Int = 0
) {
    fun getImageUri(): Uri? = if (image.isNotEmpty()) Uri.parse(image) else null
}
