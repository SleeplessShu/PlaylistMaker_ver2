package com.practicum.playlistmaker_ver2.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.practicum.playlistmaker_ver2.search.data.dto.Response
import com.practicum.playlistmaker_ver2.search.data.dto.TrackSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class RetrofitNetworkClient(
    private val iTunesApiService: ITunesApiService, private val context: Context
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) return Response().apply { resultCode = -1 }
        if (dto !is TrackSearchRequest) return Response().apply { resultCode = 400 }
        return withContext(Dispatchers.IO) {
            try {
                val response = iTunesApiService.search(dto.expression)
                response.apply { resultCode = 200 }
            } catch (e: SocketTimeoutException) {
                Response().apply { resultCode = 408 }
            } catch (e: IOException) {
                Response().apply { resultCode = -1 }
            } catch (e: HttpException) {
                Response().apply { resultCode = e.code() }
            } catch (e: Exception) {
                Response().apply { resultCode = 500 }
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities?.run {
            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || hasTransport(
                NetworkCapabilities.TRANSPORT_ETHERNET
            )
        } ?: false
    }
}
