package com.practicum.playlistmaker_ver2.player.ui

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerState
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerTrack
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerViewState
import com.practicum.playlistmaker_ver2.player.ui.models.UiState
import com.practicum.playlistmaker_ver2.utils.formatDpToPx
import com.practicum.playlistmaker_ver2.utils.serializable
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivityPlayer : AppCompatActivity() {

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

        // Установка поддержки кнопки "Назад" в Toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.setupPlayer(currentTrack.previewUrl)
        binding.bBack.setOnClickListener {
            onBackPressed()
        }
        binding.bLike.setOnClickListener { viewModel.toggleLikeButton(currentTrack) }
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
            lifecycleScope.launch {
                viewModel.checkInLiked(currentTrack)
            }
        }

        val radiusPx = formatDpToPx(8)
        Glide.with(this).load(currentTrack?.artworkUrl500).placeholder(R.drawable.placeholder)
            .fitCenter().transform(RoundedCorners(radiusPx)).into(binding.ivCollectionImage)
    }

    private fun setupObservers() {
        viewModel.getViewState().observe(this) { viewState ->
            updatePlayer(viewState)
        }
        viewModel.getUiState().observe(this) { uiState ->
            updateUi(uiState)
        }
    }

    private fun updatePlayer(viewState: PlayerViewState) {
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

    private fun updateUi(uiState: UiState) {
        binding.bLike.setImageResource(
            if (uiState.isLiked) R.drawable.ic_like_track_pushed else R.drawable.ic_like_track
        )
        currentTrack.isLiked = uiState.isLiked
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // Нажатие на кнопку "Up" в Toolbar выполняет возврат
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
