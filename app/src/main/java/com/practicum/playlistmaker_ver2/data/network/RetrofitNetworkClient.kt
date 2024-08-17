package com.practicum.playlistmaker_ver2.data.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities

import com.practicum.playlistmaker_ver2.data.dto.Response
import com.practicum.playlistmaker_ver2.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val connectivityManager: ConnectivityManager) : NetworkClient {

    private companion object {
        const val ITUNES_BASE_URL = "https://itunes.apple.com"
    }

    private val retrofit: Retrofit = Retrofit.Builder().baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()

    private val iTunesApiService = retrofit.create(ITunesApiService::class.java)
    override fun doRequest(dto: Any): Response {
        if (!isConnected()) return Response(resultCode = -1)
        if (dto !is TrackSearchRequest) return Response(resultCode = 400)
        else {
            val response = iTunesApiService.search(dto.expression).execute()
            val body = response.body() ?: Response()
            return body.apply { resultCode = response.code() }
        }
    }

    private fun isConnected(): Boolean {

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

