package com.practicum.playlistmaker_ver2.data.network

import com.practicum.playlistmaker_ver2.data.dto.TrackSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {
    @GET("/search?entity=song")
    fun search(@Query("term") expression: String): Call<TrackSearchResponse>

}
