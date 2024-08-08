package com.practicum.playlistmaker_ver2.data.dto

import com.google.gson.annotations.SerializedName


data class TrackSearchResponse(
    @SerializedName("resultCount") val resultCount: Int,
    val searchType: String,
    val expression: String,
    val results: List<TrackDto>
) : Response()
