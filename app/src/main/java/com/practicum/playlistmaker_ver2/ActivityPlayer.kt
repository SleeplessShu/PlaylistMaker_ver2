package com.practicum.playlistmaker_ver2

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker_ver2.databinding.ActivityMainBinding
import com.practicum.playlistmaker_ver2.databinding.ActivityMediatekaBinding
import com.practicum.playlistmaker_ver2.databinding.ActivityPlayerBinding


class ActivityPlayer : ActivityBase() {
    private lateinit var binding: ActivityPlayerBinding

    companion object {
        const val GET_TRACK_DATA_FROM_SEARCH = "trackData"
        const val RANDOM_TIME = "22:16"
        const val ONCLICK_REACTION = "Музыка играет"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentTrack: TrackData? = intent.getParcelableExtra(GET_TRACK_DATA_FROM_SEARCH)

        val radiusPx = formatDpToPx(this, 2)
        //временное переключение кнопок
        var isPressed_bLike = false
        var isPressed_bAddtoPlaylist = false

        binding.bBack.setOnClickListener {
            finish()
        }

        binding.tvPlayTime.text = RANDOM_TIME
        currentTrack?.let { track ->
            binding.tvTrackName.text = track.trackName
            binding.tvArtistName.text = track.artistName
            binding.tvPrimaryGenreName.text = track.primaryGenreName
            binding.tvCollectionName.text = track.collectionName
            binding.tvCountry.text = track.country
            binding.tvReleaseDate.text = track.releaseDate.substring(0, 4)
            binding.tvTrackDuration.text = formatMillisToMinutesSeconds(track.trackTimeMillis)
            Glide.with(this)
                .load(getHiResCoverArtwork(track.artworkUrl100))
                .placeholder(R.drawable.placeholder)
                .fitCenter()
                .transform(RoundedCorners(radiusPx))
                .into(binding.ivCollectionImage)
        }

        binding.bPlay.setOnClickListener {
            Toast.makeText(this, ONCLICK_REACTION, Toast.LENGTH_SHORT).show()
        }
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

    }


}