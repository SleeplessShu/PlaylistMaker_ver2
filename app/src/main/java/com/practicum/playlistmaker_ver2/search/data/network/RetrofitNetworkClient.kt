package com.practicum.playlistmaker_ver2.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.practicum.playlistmaker_ver2.search.data.dto.Response
import com.practicum.playlistmaker_ver2.search.data.dto.TrackSearchRequest
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class RetrofitNetworkClient(
    private val iTunesApiService: ITunesApiService,
    private val context: Context
) : NetworkClient {

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
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities?.run {
            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } ?: false
    }
}
