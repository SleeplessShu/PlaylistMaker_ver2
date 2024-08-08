package com.practicum.playlistmaker_ver2.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.practicum.playlistmaker_ver2.data.NetworkClient
import com.practicum.playlistmaker_ver2.data.dto.Response
import com.practicum.playlistmaker_ver2.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val context: Context) : NetworkClient {

    companion object {
        const val ITUNES_BASE_URL = "https://itunes.apple.com"
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService: ITunesApiService = retrofit.create(ITunesApiService::class.java)
    override fun doRequest(dto: Any): Response {
        if (isConnected() == false)
            return Response().apply { resultCode = -1 }
        if (dto !is TrackSearchRequest) {
            return Response().apply { resultCode = 400 }
        }
        val response = iTunesService.searchTrack(dto.expression).execute()
        val body = response.body() ?: Response()
        return if (body != null) {
            body.apply { resultCode = response.code() }
        } else {
            Response().apply { resultCode = response.code() }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }

}

