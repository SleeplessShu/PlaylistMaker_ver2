package com.practicum.playlistmaker_ver2.database.data

import androidx.room.ColumnInfo
import androidx.room.ColumnInfo.Companion.TEXT
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks_table")
data class TrackEntity(

    @PrimaryKey @ColumnInfo(name = "track_id")
    val trackId: Int,
    @ColumnInfo(name = "track_name", typeAffinity = TEXT, defaultValue = "unknown")
    val trackName: String,
    @ColumnInfo(name = "collection_name", typeAffinity = TEXT, defaultValue = "unknown")
    val collectionName: String,
    @ColumnInfo(name = "release_date", typeAffinity = TEXT, defaultValue = "unknown")
    val releaseDate: String,
    @ColumnInfo(name = "primary_genre", typeAffinity = TEXT, defaultValue = "unknown")
    val primaryGenreName: String,
    @ColumnInfo(name = "country", typeAffinity = TEXT, defaultValue = "unknown")
    val country: String,
    @ColumnInfo(name = "artist_name", typeAffinity = TEXT, defaultValue = "unknown")
    val artistName: String,
    @ColumnInfo(name = "track_time", typeAffinity = TEXT)
    val trackTime: String,
    @ColumnInfo(name = "preview_url", typeAffinity = TEXT)
    val previewUrl: String,
    @ColumnInfo(name = "poster", typeAffinity = TEXT)
    val artworkUrl500: String,

    val isLiked: Boolean = false,
    @ColumnInfo(name = "order", typeAffinity = ColumnInfo.INTEGER)
    val order: Int = 0
)
