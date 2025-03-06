package com.practicum.playlistmaker_ver2.playlist.data.repositories

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.practicum.playlistmaker_ver2.playlist.data.entities.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylist(playlist: PlaylistEntity)

    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Transaction
    suspend fun addTrackToPlaylist(playlistId: Int, newTrackId: String) {
        val playlist = getPlaylistById(playlistId) ?: return

        val updatedTrackList = if (playlist.tracksIDsList.isEmpty()) {
            newTrackId
        } else {
            "${playlist.tracksIDsList},$newTrackId"
        }

        updateTracks(playlistId, updatedTrackList, playlist.tracksCount + 1)
    }

    @Transaction
    suspend fun removeTrackFromPlaylist(playlistId: Int, trackId: String) {
        val playlist = getPlaylistById(playlistId) ?: return

        val updatedTrackList =
            playlist.tracksIDsList.split(",").filter { it != trackId }.joinToString(",")

        val newCount = (playlist.tracksCount - 1).coerceAtLeast(0)

        updateTracks(playlistId, updatedTrackList, newCount)
    }

    @Query("SELECT * FROM playlist_table")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Query("DELETE FROM playlist_table WHERE id = :playlistId")
    suspend fun deletePlaylist(playlistId: Int)

    @Query("UPDATE playlist_table SET tracksIDsList = :tracksList, tracksCount = :trackCount WHERE id = :playlistId")
    suspend fun updateTracks(playlistId: Int, tracksList: String, trackCount: Int)

    @Query("SELECT * FROM playlist_table WHERE id = :playlistId LIMIT 1")
    suspend fun getPlaylistById(playlistId: Int): PlaylistEntity?
}