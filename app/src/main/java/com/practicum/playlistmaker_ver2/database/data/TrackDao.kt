package com.practicum.playlistmaker_ver2.database.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrackToLikedDb(trackEntity: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrackEntity(trackEntity: TrackEntity)

    @Query("SELECT * FROM tracks_table WHERE track_id LIKE :id")
    suspend fun getTrackById(id: Int): TrackEntity

    @Query("SELECT * FROM tracks_table ORDER BY \"order\" ASC")
    fun getAllTracks(): Flow<List<TrackEntity>>

    @Query("SELECT MIN(`order`) FROM tracks_table")
    suspend fun getMinOrder(): Int?
}