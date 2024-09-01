package com.practicum.playlistmaker_ver2.old.data.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.practicum.playlistmaker_ver2.old.data.dto.Response
import com.practicum.playlistmaker_ver2.old.data.dto.TrackSearchRequest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

class RetrofitNetworkClient(private val connectivityManager: ConnectivityManager) : NetworkClient {

    private companion object {
        const val ITUNES_BASE_URL = "https://itunes.apple.com"
        const val TIMEOUT_SECONDS = 30L
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()


    private val retrofit: Retrofit = Retrofit.Builder().baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()

    private val iTunesApiService = retrofit.create(ITunesApiService::class.java)
    override fun doRequest(dto: Any): Response {
        if (!isConnected()) return Response(resultCode = -1)
        if (dto !is TrackSearchRequest) return Response(resultCode = 400)
        return try {
            val response = iTunesApiService.search(dto.expression).execute()
            val body = response.body() ?: Response()
            return body.apply { resultCode = response.code() }
        } catch (e: SocketTimeoutException) {
            Response(resultCode = 408)
        } catch (e: IOException) {
            Response(resultCode = -1)
        } catch (e: HttpException) {
            Response(resultCode = e.code())
        } catch (e: Exception) {
            Response(resultCode = 500)
        }
    }

    private fun isConnected(): Boolean {

        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities?.run {
            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } ?: false
    }

}
