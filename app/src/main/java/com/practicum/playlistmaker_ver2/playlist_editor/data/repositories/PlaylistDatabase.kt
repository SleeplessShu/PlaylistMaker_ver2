package com.practicum.playlistmaker_ver2.playlist_editor.data.repositories

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.practicum.playlistmaker_ver2.playlist_editor.data.converters.UriConverter
import com.practicum.playlistmaker_ver2.playlist_editor.data.entities.PlaylistEntity


@Database(
    version = 1,
    entities = [PlaylistEntity::class]

)
@TypeConverters(UriConverter::class)
abstract class PlaylistDatabase : RoomDatabase(){
    abstract fun getPlaylistDao(): PlaylistDao

    companion object{
        @Volatile
        private var INSTANCE: PlaylistDatabase? = null
    }
}