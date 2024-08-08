package com.practicum.playlistmaker_ver2.presentation

import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker_ver2.domain.models.PlayerTrack
import com.practicum.playlistmaker_ver2.domain.models.Track
import com.practicum.playlistmaker_ver2.domain.repository.PlayerControllerInterface
import com.practicum.playlistmaker_ver2.presentation.mapper.TrackToPlayerTrackMapper
import com.practicum.playlistmaker_ver2.util.DebounceClickListener
import com.practicum.playlistmaker_ver2.util.formatDpToPx
import com.practicum.playlistmaker_ver2.util.formatMillisToMinutesSeconds

class PlayerController(
    private val activity: Activity,
    private val binding: ActivityPlayerBinding,
    private val mainThreadHandler: Handler
) : PlayerControllerInterface {

    companion object {
        private const val DELAY = 200L
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val STATE_STOPPED = 4
    }

    private var playingTimeCounter: Runnable? = null
    private var playerState = STATE_DEFAULT
    private lateinit var mediaPlayer: MediaPlayer
    private var trackTime: String = ""
    private lateinit var previewUrl: String
    private lateinit var playerTrack: PlayerTrack

    override fun onCreate(currentTrack: Track) {
        var isPressed_bLike = false
        var isPressed_bAddtoPlaylist = false

        binding.bBack.setOnClickListener(DebounceClickListener {
            activity.finish()
        })
        playerTrack = TrackToPlayerTrackMapper.map(currentTrack)
        playerTrack.let { track ->
            binding.tvTrackName.text = track.trackName
            binding.tvArtistName.text = track.artistName
            binding.tvPrimaryGenreName.text = track.primaryGenreName
            binding.tvCollectionName.text = track.collectionName
            binding.tvCountry.text = track.country
            binding.tvReleaseDate.text = track.releaseDate
            binding.tvTrackDuration.text = track.trackTime
            trackTime = track.trackTime
            previewUrl = track.previewUrl
            val radiusPx = formatDpToPx(activity, 8)
            Glide.with(activity)
                .load(track.artworkUrl500)
                .placeholder(R.drawable.placeholder)
                .fitCenter()
                .transform(RoundedCorners(radiusPx))
                .into(binding.ivCollectionImage)
        }

        initializePlayer()

        binding.bLike.setOnClickListener(DebounceClickListener {
            isPressed_bLike = !isPressed_bLike
            binding.bLike.setImageResource(
                if (isPressed_bLike) R.drawable.ic_like_track_pushed else R.drawable.ic_like_track
            )
        })

        binding.bAddToPlaylist.setOnClickListener(DebounceClickListener {
            isPressed_bAddtoPlaylist = !isPressed_bAddtoPlaylist
            binding.bAddToPlaylist.setImageResource(
                if (isPressed_bAddtoPlaylist) R.drawable.ic_add_to_playlist_pushed else R.drawable.ic_add_to_playlist
            )
        })

        binding.bPlay.setOnClickListener(DebounceClickListener {
            playbackControl()
        })
    }

    override fun onPause() {
        pausePlayer()
    }

    override fun onStop() {
        stopPlayer()
    }

    override fun onDestroy() {
        mediaPlayer.release()
        playingTimeCounter?.let {
            mainThreadHandler.removeCallbacks(it)
        }
        binding.tvPlayTime.text = "00:00"
    }

    override fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> pausePlayer()
            STATE_PREPARED, STATE_STOPPED, STATE_PAUSED -> startPlayer()
        }
    }

    private fun initializePlayer() {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(previewUrl)
            prepareAsync()
            setOnPreparedListener {
                playerState = STATE_PREPARED
            }
            setOnCompletionListener {
                binding.bPlay.setImageResource(R.drawable.ic_play)
                playerState = STATE_STOPPED
                playingTimeCounter?.let { mainThreadHandler.removeCallbacks(it) }
                binding.tvPlayTime.text = "00:00"
            }
            setOnErrorListener { _, what, extra ->
                Toast.makeText(
                    activity,
                    "Error occurred: $what, $extra",
                    Toast.LENGTH_LONG
                ).show()
                true
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        binding.bPlay.setImageResource(R.drawable.ic_pause)
        playerState = STATE_PLAYING
        startPlayingTimeCounter()
    }

    private fun pausePlayer() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            binding.bPlay.setImageResource(R.drawable.ic_play)
            playerState = STATE_PAUSED
            playingTimeCounter?.let {
                mainThreadHandler.removeCallbacks(it)
            }
        }
    }

    private fun stopPlayer() {
        mediaPlayer.stop()
        binding.bPlay.setImageResource(R.drawable.ic_play)
        playerState = STATE_STOPPED
        playingTimeCounter?.let {
            mainThreadHandler.removeCallbacks(it)
        }
        binding.tvPlayTime.text = "00:00"
    }

    private fun startPlayingTimeCounter() {
        playingTimeCounter = createPlayingTimeCounterTask()
        mainThreadHandler.post(playingTimeCounter!!)
    }

    private fun createPlayingTimeCounterTask(): Runnable {
        return object : Runnable {
            override fun run() {
                binding.tvPlayTime.text =
                    formatMillisToMinutesSeconds(mediaPlayer.currentPosition.toLong())
                mainThreadHandler.postDelayed(this, DELAY)
            }
        }
    }
}
