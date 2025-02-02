package com.practicum.playlistmaker_ver2

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker_ver2.database.data.TrackDao
import com.practicum.playlistmaker_ver2.database.data.TrackEntity
import com.practicum.playlistmaker_ver2.favorite.data.db.dao.FavoriteDao
import com.practicum.playlistmaker_ver2.favorite.data.db.entity.FavoriteEntity
import com.practicum.playlistmaker_ver2.playlist.data.database.dao.PlaylistDao
import com.practicum.playlistmaker_ver2.playlist.data.database.dao.PlaylistTrackDao
import com.practicum.playlistmaker_ver2.playlist.data.database.entity.PlaylistEntity
import com.practicum.playlistmaker_ver2.playlist.data.database.entity.PlaylistTrackEntity

@Database(
    version = 1,
    entities = [FavoriteEntity::class, TrackEntity::class, PlaylistEntity::class, PlaylistTrackEntity::class]
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun trackDao(): TrackDao

    abstract fun playListDao(): PlaylistDao

    abstract fun playListTrackDao(): PlaylistTrackDao
}