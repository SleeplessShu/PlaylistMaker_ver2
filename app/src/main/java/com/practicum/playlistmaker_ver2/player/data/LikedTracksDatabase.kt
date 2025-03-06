package com.practicum.playlistmaker_ver2.player.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.practicum.playlistmaker_ver2.player.data.entities.TrackEntity

@Database(
    version = 4,
    entities = [
        TrackEntity::class
    ]

)
abstract class LikedTracksDatabase : RoomDatabase() {
    abstract fun getTrackDao(): TrackDao

    companion object {
        @Volatile
        private var INSTANCE: LikedTracksDatabase? = null

        fun getInstance(context: Context): LikedTracksDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LikedTracksDatabase::class.java,
                    "liked_tracks.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}