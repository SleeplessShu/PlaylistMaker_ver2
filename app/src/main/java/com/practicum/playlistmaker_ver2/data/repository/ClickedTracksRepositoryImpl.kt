package com.practicum.playlistmaker_ver2.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker_ver2.data.dto.TrackDto
import com.practicum.playlistmaker_ver2.domain.models.Track
import com.practicum.playlistmaker_ver2.domain.repository.ClickedTracksRepository

class ClickedTracksRepositoryImpl(context: Context) :
    ClickedTracksRepository {

    companion object {
        const val CLICKED_TRACKS_REPOSITORY_KEY: String = "clicked_tracks"
        const val CLICKED_TRACKS_REPOSITORY_NAME: String = "previous_search_result"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        CLICKED_TRACKS_REPOSITORY_NAME, Context.MODE_PRIVATE
    )

    override fun addClickedTrack(track: Track) {
        val previousSearchList: ArrayList<Track> = getClickedTracks()
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
        editor.putString(CLICKED_TRACKS_REPOSITORY_KEY, value)
        editor.apply()
    }


    override fun getClickedTracks(): ArrayList<Track> {
        val json = sharedPreferences.getString(CLICKED_TRACKS_REPOSITORY_KEY, null)
        return if (json != null) {
            val type = object : TypeToken<ArrayList<TrackDto>>() {}.type
            Gson().fromJson(json, type)
        } else {
            ArrayList()
        }
    }

    override fun eraseClickedTracks() {
        val editor = sharedPreferences.edit()
        editor.remove(CLICKED_TRACKS_REPOSITORY_KEY)
        editor.apply()
    }
}