package com.practicum.playlistmaker_ver2.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker_ver2.search.domain.models.Track
import com.practicum.playlistmaker_ver2.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker_ver2.player.domain.models.PlayerTrack
import com.practicum.playlistmaker_ver2.player.ui.mappers.TrackToPlayerTrackMapper

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    companion object {
        fun provideFactory(interactor: PlayerInteractor): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
                        return PlayerViewModel(interactor) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }

    val playerTrack: LiveData<PlayerTrack> = playerInteractor.playerTrack
    val isPlaying: LiveData<Boolean> = playerInteractor.isPlaying
    val currentTime: LiveData<Long> = playerInteractor.currentTime
    val errorMessage: LiveData<String> = playerInteractor.errorMessage

    fun initializePlayer(currentTrack: Track) {
        // Преобразование трека с помощью маппера и передача в интерактор
        val playerTrack = TrackToPlayerTrackMapper.map(currentTrack)
        playerInteractor.setTrack(playerTrack)
    }

    fun playPause() {
        playerInteractor.playPause()
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
    }
}
