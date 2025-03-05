package com.practicum.playlistmaker_ver2.playlist.data.repositories

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.practicum.playlistmaker_ver2.playlist.data.entities.TrackInPlaylistEntity

@Database(
    version = 1, entities = [TrackInPlaylistEntity::class]
)

abstract class TracksInPlaylistsDatabase : RoomDatabase() {
    abstract fun getTracksInPlaylistsDao(): TracksInPlaylistsDao

    companion object {
        @Volatile
        private var INSTANCE: TracksInPlaylistsDatabase? = null

        fun getInstance(context: Context): TracksInPlaylistsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TracksInPlaylistsDatabase::class.java,
                    "tracksInPlaylists.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}