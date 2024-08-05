package com.practicum.playlistmaker_ver2.domain.repository

import com.practicum.playlistmaker_ver2.domain.models.Track


interface TracksRepositorySharedPreferences {
    fun putDataToSharedPreferences(track: Track)
    fun getDataFromSharedPreferences(): List<Track>
    fun eraseDataInSharedPreferences()
}