package com.practicum.playlistmaker_ver2.search.data.dto


data class TrackSearchResponse(

    val searchType: String,
    val expression: String,
    val results: List<TrackDto>
) : Response()
