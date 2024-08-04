package com.practicum.playlistmaker_ver2.data.network

import com.google.gson.annotations.SerializedName
import com.practicum.playlistmaker_ver2.data.dto.TrackData

class ApiResponseITunes(
    @SerializedName("resultCount") val resultCount: Int,
    @SerializedName("results") val results: List<TrackData>
)
