package com.practicum.playlistmaker_ver2.favorite.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker_ver2.favorite.data.db.entity.FavoriteEntity

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: FavoriteEntity)

    @Query("SELECT * FROM track_favorite")
    suspend fun getTracks(): List<FavoriteEntity>

    @Query("SELECT trackId FROM track_favorite")
    suspend fun getTracksIds(): List<Long>

    @Query("SELECT * FROM track_favorite WHERE timeAdd ORDER BY timeAdd DESC")
    suspend fun sortTrack():List<FavoriteEntity>


    @Delete(entity = FavoriteEntity::class)
    suspend fun deleteTrackEntity(favoriteEntity: FavoriteEntity)
}