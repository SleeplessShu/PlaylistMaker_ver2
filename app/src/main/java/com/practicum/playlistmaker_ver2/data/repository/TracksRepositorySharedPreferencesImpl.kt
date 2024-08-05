package com.practicum.playlistmaker_ver2.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker_ver2.data.dto.TrackData
import com.practicum.playlistmaker_ver2.domain.models.Track
import com.practicum.playlistmaker_ver2.domain.repository.TracksRepositorySharedPreferences

class TracksRepositorySharedPreferencesImpl(context: Context) :
    TracksRepositorySharedPreferences {

    companion object {
        const val TRACK_REPOSITORY_SHARED_PREFERENCES_KEY: String = "clicked_tracks"
        const val TRACK_REPOSITORY_SHARED_PREFERENCES_NAME: String = "previous_search_result"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        TRACK_REPOSITORY_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE
    )

    override fun putDataToSharedPreferences(track: Track) {
        val previousSearchList: ArrayList<Track> = getDataFromSharedPreferences()
        val iterator = previousSearchList.iterator()
        var found = false

        while (iterator.hasNext()) {
            val t = iterator.next()
            if (t.trackId == track.trackId) {
                iterator.remove()
                found = true
                break
            }
        }

        previousSearchList.add(0, track)

        if (!found && previousSearchList.size > 10) {
            previousSearchList.removeAt(previousSearchList.size - 1)
        }

        val value = Gson().toJson(previousSearchList)
        val editor = sharedPreferences.edit()
        editor.putString(TRACK_REPOSITORY_SHARED_PREFERENCES_KEY, value)
        editor.apply()
    }


    override fun getDataFromSharedPreferences(): ArrayList<Track> {
        val json = sharedPreferences.getString(TRACK_REPOSITORY_SHARED_PREFERENCES_KEY, null)
        return if (json != null) {
            val type = object : TypeToken<ArrayList<TrackData>>() {}.type
            Gson().fromJson(json, type)
        } else {
            ArrayList()
        }
    }

    override fun eraseDataInSharedPreferences() {
        val editor = sharedPreferences.edit()
        editor.remove(TRACK_REPOSITORY_SHARED_PREFERENCES_KEY)
        editor.apply()
    }
}