package com.practicum.playlistmaker_ver2.domain.repository

import com.practicum.playlistmaker_ver2.domain.models.Track


interface TracksRepositoryInSharedPreferences {
    fun putDataToSharedPreferences(track: Track)
    fun getDataFromSharedPreferences(): List<Track>
    fun eraseDataInSharedPreferences()
}