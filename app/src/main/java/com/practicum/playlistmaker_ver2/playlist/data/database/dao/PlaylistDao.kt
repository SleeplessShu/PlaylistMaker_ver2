package com.practicum.playlistmaker_ver2.playlist.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.practicum.playlistmaker_ver2.playlist.data.database.entity.PlaylistEntity

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayList(playList: List<PlaylistEntity>)
    @Query("SELECT * FROM play_list_entity")
    suspend fun getPlayList(): List<PlaylistEntity>

    @Query("SELECT * FROM play_list_entity WHERE playListId =:id ")
    suspend fun getPlayListId(id:Int):PlaylistEntity

    @Query("SELECT COUNT(trackId)  FROM play_list_entity ")
    suspend fun getCount():Int

    @Delete(entity = PlaylistEntity::class)
    suspend fun deletePlayList(listEntity: PlaylistEntity)

    @Update(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlayList(playList: PlaylistEntity)
}