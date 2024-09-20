package com.practicum.playlistmaker_ver2.player.ui


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker_ver2.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerTrack
import java.util.concurrent.TimeUnit

class PlayerViewModel(
    private val track: PlayerTrack, private val playerInteractor: PlayerInteractor
) : ViewModel() {
    init {
        playerInteractor.setStateChangeListener { state, currentTime, errorMessage ->
            viewState.value = viewState.value?.copy(
                playerState = state,
                currentTime = convertTime(currentTime),
                errorMessage = errorMessage
            )
            Log.d("STATE", state.toString())
        }

    }

    private val viewState = MutableLiveData(PlayerViewState())
    fun observeViewState(): LiveData<PlayerViewState> = viewState

    fun initializePlayer(currentTrack: PlayerTrack) {
        currentTrack.previewUrl?.let { playerInteractor.setTrackUrl(it) }
        Log.d("DEBUG", "initializePlayer: ${currentTrack.previewUrl}")
    }

    fun playPause() {
        playerInteractor.playPause()
    }

    fun releasePlayer() {
        playerInteractor.release()
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
    }
    private fun convertTime(currentTime: Long): String {
        return if (currentTime == 0L) {
            "00:00"
        } else {
            val hours = TimeUnit.MILLISECONDS.toHours(currentTime)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(currentTime) % 60
            val seconds = TimeUnit.MILLISECONDS.toSeconds(currentTime) % 60
            if (hours > 0) {
                String.format("%02d:%02d:%02d", hours, minutes, seconds)
            } else {
                String.format("%02d:%02d", minutes, seconds)
            }
        }
    }

    companion object {
        fun provideFactory(
            interactor: PlayerInteractor,
            track: PlayerTrack
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
                        return PlayerViewModel(track, interactor) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}
