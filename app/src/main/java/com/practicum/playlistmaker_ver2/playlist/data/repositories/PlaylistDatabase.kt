package com.practicum.playlistmaker_ver2.playlist.data.repositories

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.practicum.playlistmaker_ver2.playlist.data.converters.UriConverter
import com.practicum.playlistmaker_ver2.playlist.data.entities.PlaylistEntity


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