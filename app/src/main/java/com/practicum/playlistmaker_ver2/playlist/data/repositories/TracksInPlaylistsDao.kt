package com.practicum.playlistmaker_ver2.playlist.data.repositories

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker_ver2.playlist.data.entities.TrackInPlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TracksInPlaylistsDao {
    @Insert(entity = TrackInPlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackToTracksInPlaylists(trackEntity: TrackInPlaylistEntity)

    @Delete(entity = TrackInPlaylistEntity::class)
    suspend fun deleteTrackEntity(trackEntity: TrackInPlaylistEntity)

    @Query("SELECT * FROM tracksInPlaylists_table WHERE track_id LIKE :id")
    suspend fun getTrackById(id: Int): TrackInPlaylistEntity

    @Query("SELECT * FROM tracksInPlaylists_table ORDER BY \"order\" ASC")
    fun getAllTracks(): Flow<List<TrackInPlaylistEntity>>

    @Query("SELECT MIN(`order`) FROM tracksInPlaylists_table")
    suspend fun getMinOrder(): Int?
}
