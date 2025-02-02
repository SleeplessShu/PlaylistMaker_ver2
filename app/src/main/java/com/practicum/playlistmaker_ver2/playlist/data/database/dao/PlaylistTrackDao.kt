package com.practicum.playlistmaker_ver2.playlist.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker_ver2.playlist.data.database.entity.PlaylistTrackEntity

@Dao
interface PlaylistTrackDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlayListTrack(playListTrack: PlaylistTrackEntity)

    @Query("SELECT * FROM track_play_list WHERE timeAdd ORDER BY timeAdd DESC")
    suspend fun getPlayListTrackId(): List<PlaylistTrackEntity>

    @Query("SELECT * FROM track_play_list WHERE trackId=:id")
    suspend fun getTrackDb(id: Long): PlaylistTrackEntity
    @Delete(entity = PlaylistTrackEntity::class)
    suspend fun deleteTrack(playListTrack: PlaylistTrackEntity)
}