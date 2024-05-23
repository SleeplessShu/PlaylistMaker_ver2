package com.practicum.playlistmaker_ver2

import android.graphics.Movie
import com.google.gson.annotations.SerializedName

class iTunesResponse(
    @SerializedName("resultCount") val resultCount: Int,
    @SerializedName("results") val results: List<TrackData>
)
