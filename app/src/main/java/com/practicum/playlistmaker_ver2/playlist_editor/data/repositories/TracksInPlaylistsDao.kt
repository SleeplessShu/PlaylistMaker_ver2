package com.practicum.playlistmaker_ver2.playlist_editor.data.repositories

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker_ver2.playlist_editor.data.entities.TrackInPlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TracksInPlaylistsDao {
    @Insert(entity = TrackInPlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrackToTracksInPlaylists(trackEntity: TrackInPlaylistEntity)

    @Delete(entity = TrackInPlaylistEntity::class)
    suspend fun deleteTrackEntity(trackEntity: TrackInPlaylistEntity)

    @Query("SELECT * FROM tracksInPlaylists_table WHERE track_id = :id")
    suspend fun getTrackById(id: Int): TrackInPlaylistEntity

    @Query("SELECT * FROM tracksInPlaylists_table ORDER BY \"order\" ASC")
    fun getAllTracks(): Flow<List<TrackInPlaylistEntity>>

    @Query("SELECT MIN(`order`) FROM tracksInPlaylists_table")
    suspend fun getMinOrder(): Int?

    @Query("SELECT playlistsIDs FROM tracksInPlaylists_table WHERE track_id = :id")
    suspend fun getPlaylistsIDsByTrackId(id: Int): String


}
