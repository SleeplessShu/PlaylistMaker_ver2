package com.practicum.playlistmaker_ver2.playlist.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "play_list_entity")
data class PlaylistEntity (
    @PrimaryKey(autoGenerate = true)
    val playListId:Int,
    val namePlayList:String,
    val image:String,
    val description:String,
    val filePath:String,
    val count:Int,
    val trackId:String,
)
