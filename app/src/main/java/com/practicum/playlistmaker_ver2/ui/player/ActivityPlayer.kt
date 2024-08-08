package com.practicum.playlistmaker_ver2.ui.player

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker_ver2.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker_ver2.domain.models.Track
import com.practicum.playlistmaker_ver2.presentation.PlayerController
import com.practicum.playlistmaker_ver2.ui.base.ActivityBase
import com.practicum.playlistmaker_ver2.util.Creator

import com.practicum.playlistmaker_ver2.util.serializable

class ActivityPlayer : ActivityBase() {
    companion object {
        const val GET_TRACK_DATA_FROM_SEARCH = "trackData"
    }

    private lateinit var binding: ActivityPlayerBinding
    private var mainThreadHandler: Handler? = null

    private val playerController by lazy {
        Creator.providePlayerControllerUseCase(
            activity = this,
            binding = binding,
            mainThreadHandler = Handler(Looper.getMainLooper())
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainThreadHandler = Handler(Looper.getMainLooper())
        val currentTrack: Track? = intent.serializable(GET_TRACK_DATA_FROM_SEARCH)
        currentTrack?.let { playerController.onCreate(it) }
    }

    override fun onPause() {
        super.onPause()
        playerController.onPause()
    }

    override fun onStop() {
        super.onStop()
        playerController.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerController.onDestroy()
    }
}
