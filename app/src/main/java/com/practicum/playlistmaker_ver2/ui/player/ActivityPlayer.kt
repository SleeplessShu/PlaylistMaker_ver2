package com.practicum.playlistmaker_ver2.ui.player

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker_ver2.domain.models.PlayerTrack
import com.practicum.playlistmaker_ver2.domain.models.Track
import com.practicum.playlistmaker_ver2.presentation.mapper.TrackToPlayerTrackMapper
import com.practicum.playlistmaker_ver2.ui.base.ActivityBase
import com.practicum.playlistmaker_ver2.util.Creator
import com.practicum.playlistmaker_ver2.util.formatDpToPx
import com.practicum.playlistmaker_ver2.util.serializable
import java.util.concurrent.TimeUnit

class ActivityPlayer : ActivityBase(), PlayerController.PlayerListener {

    private lateinit var binding: ActivityPlayerBinding
    private val mainThreadHandler = Handler(Looper.getMainLooper())

    private companion object {
        const val GET_TRACK_DATA_FROM_SEARCH = "trackData"
    }


    private val playerController by lazy {
        Creator.providePlayerController(
            mainThreadHandler = mainThreadHandler,
            playerListener = this,
            trackToPlayerTrackMapper = TrackToPlayerTrackMapper
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentTrack: Track? = intent.serializable(GET_TRACK_DATA_FROM_SEARCH)
        currentTrack?.let { playerController.onCreate(it) }

        binding.bBack.setOnClickListener { finish() }

        binding.bLike.setOnClickListener {
            toggleLikeButton()
        }

        binding.bAddToPlaylist.setOnClickListener {
            toggleAddToPlaylistButton()
        }

        binding.bPlay.setOnClickListener {
            playerController.playbackControl()
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

    override fun onTrackLoaded(track: PlayerTrack) {
        binding.apply {
            tvTrackName.text = track.trackName
            tvArtistName.text = track.artistName
            tvPrimaryGenreName.text = track.primaryGenreName
            tvCollectionName.text = track.collectionName
            tvCountry.text = track.country
            tvReleaseDate.text = track.releaseDate
            tvTrackDuration.text = track.trackTime
        }

        val radiusPx = formatDpToPx(8)
        Glide.with(this)
            .load(track.artworkUrl500)
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(radiusPx))
            .into(binding.ivCollectionImage)
    }

    override fun onPlayerPrepared() {

    }

    override fun onPlayerStarted() {
        binding.bPlay.setImageResource(R.drawable.ic_pause)
    }

    override fun onPlayerPaused() {
        binding.bPlay.setImageResource(R.drawable.ic_play)
    }

    override fun onPlayerStopped() {
        binding.bPlay.setImageResource(R.drawable.ic_play)
        binding.tvPlayTime.text = "00:00"
    }

    override fun onPlayTimeUpdate(currentTime: Long) {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(currentTime)
        val seconds =
            TimeUnit.MILLISECONDS.toSeconds(currentTime) - TimeUnit.MINUTES.toSeconds(minutes)
        binding.tvPlayTime.text = String.format("%02d:%02d", minutes, seconds)
    }

    override fun onPlayerError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
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