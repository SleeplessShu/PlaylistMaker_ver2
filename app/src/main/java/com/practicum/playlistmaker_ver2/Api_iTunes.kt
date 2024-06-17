package com.practicum.playlistmaker_ver2

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api_iTunes {
    @GET("/search?entity=song")
    fun searchTrack(@Query("term") text: String): Call<Api_Response_iTunes>
}
