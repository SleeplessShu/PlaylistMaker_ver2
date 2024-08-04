package com.practicum.playlistmaker_ver2.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiITunes {
    @GET("/search?entity=song")
    fun searchTrack(@Query("term") text: String): Call<ApiResponseITunes>
}
