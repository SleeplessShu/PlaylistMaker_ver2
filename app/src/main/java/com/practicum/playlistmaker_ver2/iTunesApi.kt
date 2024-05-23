package com.practicum.playlistmaker_ver2

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface iTunesApi {
    @GET("/search?entity=song")
    fun searchTrack(@Query("term") text: String): Call<iTunesResponse>
}
