package com.practicum.playlistmaker_ver2

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker_ver2.databinding.ActivityPlayerBinding
import kotlinx.coroutines.Runnable
import kotlin.properties.Delegates


class ActivityPlayer : ActivityBase() {
    private lateinit var binding: ActivityPlayerBinding

    companion object {
        const val GET_TRACK_DATA_FROM_SEARCH = "trackData"
        private const val DELAY = 300L
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    private var mainThreadHandler: Handler? = null
    private var playingTimeCounter: Runnable? = null
    private var playerState = STATE_DEFAULT
    private lateinit var mediaPlayer: MediaPlayer
    private var trackTime by Delegates.notNull<Long>()
    private lateinit var previewUrl: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)



        mainThreadHandler = Handler(Looper.getMainLooper())

        val currentTrack: TrackData? = intent.getParcelableExtra(GET_TRACK_DATA_FROM_SEARCH)

        val radiusPx = formatDpToPx(this, 2)

        //---->временное переключение кнопок<----
        var isPressed_bLike = false
        var isPressed_bAddtoPlaylist = false

        binding.bBack.setOnClickListener {
            finish()
        }

        currentTrack?.let { track ->
            binding.tvTrackName.text = track.trackName
            binding.tvArtistName.text = track.artistName
            binding.tvPrimaryGenreName.text = track.primaryGenreName
            binding.tvCollectionName.text = track.collectionName
            binding.tvCountry.text = track.country
            binding.tvReleaseDate.text = track.releaseDate.substring(0, 4)
            binding.tvTrackDuration.text = formatMillisToMinutesSeconds(track.trackTimeMillis)
            trackTime = track.trackTimeMillis
            previewUrl = track.previewUrl
            Glide.with(this)
                .load(getHiResCoverArtwork(track.artworkUrl100))
                .placeholder(R.drawable.placeholder)
                .fitCenter()
                .transform(RoundedCorners(radiusPx))
                .into(binding.ivCollectionImage)
        }
        initializePlayer()
        binding.bLike.setOnClickListener {
            if (!isPressed_bLike) {
                isPressed_bLike = true
                binding.bLike.setImageResource(R.drawable.ic_like_track_pushed)
            } else {
                isPressed_bLike = false
                binding.bLike.setImageResource(R.drawable.ic_like_track)
            }
        }
        binding.bAddToPlaylist.setOnClickListener {
            if (!isPressed_bAddtoPlaylist) {
                isPressed_bAddtoPlaylist = true
                binding.bAddToPlaylist.setImageResource(R.drawable.ic_add_to_playlist_pushed)
            } else {
                isPressed_bAddtoPlaylist = false
                binding.bAddToPlaylist.setImageResource(R.drawable.ic_add_to_playlist)
            }
        }

        binding.bPlay.setOnClickListener {
            playbackControl()
        }

    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        playingTimeCounter?.let {
            mainThreadHandler?.removeCallbacks(it)
        }
        binding.tvPlayTime.text = "00:00"
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> pausePlayer()
            STATE_PREPARED, STATE_PAUSED -> startPlayer()
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
                playerState = STATE_PREPARED
            }
            setOnErrorListener { mp, what, extra ->
                Toast.makeText(
                    this@ActivityPlayer,
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
        mediaPlayer.pause()
        binding.bPlay.setImageResource(R.drawable.ic_play)
        playerState = STATE_PAUSED
        playingTimeCounter?.let {
            mainThreadHandler?.removeCallbacks(it)
        }
    }

    private fun startPlayingTimeCounter() {
        playingTimeCounter = createPlayingTimeCounterTask()
        mainThreadHandler?.post(playingTimeCounter!!)
    }

    private fun createPlayingTimeCounterTask(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    binding.tvPlayTime.text =
                        formatMillisToMinutesSeconds(mediaPlayer.currentPosition.toLong())
                    mainThreadHandler?.postDelayed(this, DELAY)
                }

            }
        }
    }
}
