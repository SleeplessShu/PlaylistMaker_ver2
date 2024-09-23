package com.practicum.playlistmaker_ver2.search.data.repository


import android.content.SharedPreferences

import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker_ver2.search.domain.models.Track
import com.practicum.playlistmaker_ver2.search.domain.repository.ClickedTracksRepository

class ClickedTracksRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) :
    ClickedTracksRepository {

    override fun addTrack(track: Track) {
        var previousSearchList: ArrayList<Track> = getTracks()
        previousSearchList = ArrayList(previousSearchList.filter { it.trackId != track.trackId })
        if (previousSearchList.size == 10) {
            previousSearchList.removeAt(previousSearchList.size - 1)
        }
        previousSearchList.add(0, track)
        val value = gson.toJson(previousSearchList)
        sharedPreferences.edit {
            putString(CLICKED_TRACKS_REPOSITORY_KEY, value)
        }
    }


    override fun getTracks(): ArrayList<Track> {
        val json = sharedPreferences.getString(CLICKED_TRACKS_REPOSITORY_KEY, null)
        return if (json != null) {
            val type = object : TypeToken<ArrayList<Track>>() {}.type
            gson.fromJson(json, type)
        } else {
            ArrayList()
        }
    }

    override fun eraseTracks() {
        sharedPreferences.edit {
            remove(CLICKED_TRACKS_REPOSITORY_KEY)
        }
    }

    private companion object {
        const val CLICKED_TRACKS_REPOSITORY_KEY: String = "clicked_tracks"

    }
}