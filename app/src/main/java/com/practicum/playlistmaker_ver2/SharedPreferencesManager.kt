package com.practicum.playlistmaker_ver2

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferencesManager(context: Context) {
    private val sharedPreferencesName: String = "previous_search_result"
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        sharedPreferencesName, Context.MODE_PRIVATE
    )
    private val sharedPreferencesKey: String = "clicked_tracks"

    fun saveData(key: String, track: TrackData) {
        val previousSearchList: ArrayList<TrackData> = getData(sharedPreferencesKey)
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
        editor.putString(key, value)
        editor.apply()
    }

    fun getData(key: String): ArrayList<TrackData> {
        val json = sharedPreferences.getString(key, null)
        return if (json != null) {
            val type = object : TypeToken<ArrayList<TrackData>>() {}.type
            Gson().fromJson(json, type)
        } else {
            ArrayList()
        }
    }

    fun removeData(key: String) {
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }

    fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }
}
