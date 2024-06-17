package com.practicum.playlistmaker_ver2

import com.google.gson.annotations.SerializedName

class Api_Response_iTunes(
    @SerializedName("resultCount") val resultCount: Int,
    @SerializedName("results") val results: List<TrackData>
)
