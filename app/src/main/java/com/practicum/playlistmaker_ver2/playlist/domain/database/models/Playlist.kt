package com.practicum.playlistmaker_ver2.playlist.domain.database.models

data class Playlist (
    val playListId:Int,
    val namePlayList:String,
    val image:String,
    val description:String,
    val filePath:String,
    val count:Int,
    val trackId:List<Long>,
)
