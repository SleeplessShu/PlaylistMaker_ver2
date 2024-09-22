package com.practicum.playlistmaker_ver2.player.ui

import android.os.Bundle
import android.widget.Toast

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker_ver2.base.ActivityBase
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerState
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerTrack
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerViewState
import com.practicum.playlistmaker_ver2.util.formatDpToPx
import com.practicum.playlistmaker_ver2.util.serializable
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivityPlayer : ActivityBase() {

    private lateinit var binding: ActivityPlayerBinding

    private val viewModel: PlayerViewModel by viewModel()

    private val currentTrack: PlayerTrack by lazy {
        requireNotNull(
            intent.serializable(
                GET_TRACK_DATA_FROM_SEARCH
            )
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupStatusBar(androidx.appcompat.R.attr.colorPrimary)
        viewModel.setupPlayer(currentTrack.previewUrl)
        binding.bBack.setOnClickListener { finish() }
        binding.bLike.setOnClickListener { toggleLikeButton() }
        binding.bAddToPlaylist.setOnClickListener { toggleAddToPlaylistButton() }
        binding.bPlay.setOnClickListener { viewModel.playPause() }
        setupObservers()
        initializeViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releasePlayer()
    }

    private fun initializeViews() {
        binding.apply {
            tvTrackName.text = currentTrack?.trackName
            tvArtistName.text = currentTrack?.artistName
            tvPrimaryGenreName.text = currentTrack?.primaryGenreName
            tvCollectionName.text = currentTrack?.collectionName
            tvCountry.text = currentTrack?.country
            tvReleaseDate.text = currentTrack?.releaseDate
            tvTrackDuration.text = currentTrack?.trackTime
            binding.tvPlayTime.text = "00:00"
        }
        val radiusPx = formatDpToPx(8)
        Glide.with(this).load(currentTrack?.artworkUrl500)
            .placeholder(R.drawable.placeholder).fitCenter().transform(RoundedCorners(radiusPx))
            .into(binding.ivCollectionImage)
    }

    private fun setupObservers() {
        viewModel.getViewState().observe(this) { viewState ->
            updateUi(viewState)
        }
    }

    private fun updateUi(viewState: PlayerViewState) {
        binding.tvPlayTime.text = viewState.currentTime
        when (viewState.playerState) {
            PlayerState.DEFAULT -> {
                binding.bPlay.isEnabled = false
            }

            PlayerState.PREPARED, PlayerState.PAUSED, PlayerState.STOPPED -> {
                binding.bPlay.setImageResource(R.drawable.ic_play)
                binding.bPlay.isEnabled = true
            }

            PlayerState.PLAYING -> {
                binding.bPlay.setImageResource(R.drawable.ic_pause)
            }

            PlayerState.ERROR -> {
                binding.bPlay.setImageResource(R.drawable.ic_play)
                Toast.makeText(this, viewState.errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun toggleLikeButton() {
        val isPressed = binding.bLike.tag == true
        binding.bLike.tag = !isPressed
        binding.bLike.setImageResource(
            if (!isPressed) R.drawable.ic_like_track_pushed else R.drawable.ic_like_track
        )
    }

    private fun toggleAddToPlaylistButton() {
        val isPressed = binding.bAddToPlaylist.tag == true
        binding.bAddToPlaylist.tag = !isPressed
        binding.bAddToPlaylist.setImageResource(
            if (!isPressed) R.drawable.ic_add_to_playlist_pushed else R.drawable.ic_add_to_playlist
        )
    }

    private companion object {
        const val GET_TRACK_DATA_FROM_SEARCH = "trackData"
    }
}
