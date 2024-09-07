package com.practicum.playlistmaker_ver2.player.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker_ver2.search.domain.models.Track
import com.practicum.playlistmaker_ver2.base.ActivityBase
import com.practicum.playlistmaker_ver2.creator.Creator
import com.practicum.playlistmaker_ver2.util.formatDpToPx
import com.practicum.playlistmaker_ver2.util.serializable
import java.util.concurrent.TimeUnit

class ActivityPlayer : ActivityBase() {

    private lateinit var binding: ActivityPlayerBinding
    private val viewModel: PlayerViewModel by viewModels {
        PlayerViewModel.provideFactory(Creator.providePlayerInteractor())
    }

    private companion object {
        const val GET_TRACK_DATA_FROM_SEARCH = "trackData"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()

        val currentTrack: Track? = intent.serializable(GET_TRACK_DATA_FROM_SEARCH)
        currentTrack?.let { viewModel.initializePlayer(it) }

        binding.bBack.setOnClickListener { finish() }
        binding.bLike.setOnClickListener { toggleLikeButton() }
        binding.bAddToPlaylist.setOnClickListener { toggleAddToPlaylistButton() }
        binding.bPlay.setOnClickListener { viewModel.playPause() }
    }

    private fun setupObservers() {
        viewModel.playerTrack.observe(this, Observer { track ->
            track?.let {
                binding.apply {
                    tvTrackName.text = it.trackName
                    tvArtistName.text = it.artistName
                    tvPrimaryGenreName.text = it.primaryGenreName
                    tvCollectionName.text = it.collectionName
                    tvCountry.text = it.country
                    tvReleaseDate.text = it.releaseDate
                    tvTrackDuration.text = it.trackTime
                }
                val radiusPx = formatDpToPx(8)
                Glide.with(this)
                    .load(it.artworkUrl500)
                    .placeholder(R.drawable.placeholder)
                    .fitCenter()
                    .transform(RoundedCorners(radiusPx))
                    .into(binding.ivCollectionImage)
            }
        })

        viewModel.isPlaying.observe(this, Observer { isPlaying ->
            binding.bPlay.setImageResource(if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play)
        })

        viewModel.currentTime.observe(this, Observer { currentTime ->
            val minutes = TimeUnit.MILLISECONDS.toMinutes(currentTime)
            val seconds =
                TimeUnit.MILLISECONDS.toSeconds(currentTime) - TimeUnit.MINUTES.toSeconds(minutes)
            binding.tvPlayTime.text = String.format("%02d:%02d", minutes, seconds)
        })

        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        })
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
}
