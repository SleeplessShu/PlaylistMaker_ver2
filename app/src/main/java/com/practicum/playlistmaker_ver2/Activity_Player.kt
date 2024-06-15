package com.practicum.playlistmaker_ver2

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class Activity_Player : Activity_Base() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val currentTrack: TrackData? = intent.getParcelableExtra("trackData")

        val bBack = findViewById<ImageView>(R.id.bBack)
        val bPlay = findViewById<ImageView>(R.id.bPlay)
        val bLike = findViewById<ImageButton>(R.id.bLike)
        val bAddToPlaylist = findViewById<ImageButton>(R.id.bAddToPlaylist)

        val tvPlayTime = findViewById<TextView>(R.id.tvPlayTime)
        val tvPrimaryGenreName = findViewById<TextView>(R.id.tvPrimaryGenreName)
        val tvCollectionName = findViewById<TextView>(R.id.tvCollectionName)
        val tvCountry = findViewById<TextView>(R.id.tvCountry)
        val tvReleaseDate = findViewById<TextView>(R.id.tvReleaseDate)
        val tvTrackDuration = findViewById<TextView>(R.id.tvTrackDuration)
        val ivCollectionImage = findViewById<ImageView>(R.id.ivCollectionImage)

        val radiusPx = dpToPx(this, 2)
        //временное переключение кнопок
        var isPressed_bLike = false
        var isPressed_bAddtoPlaylist = false

        bBack.setOnClickListener {
            finish()
        }

        tvPlayTime.text = "00:00" //НЕ ПОНЯТНО ЧЕМ ЗАПОЛНЯТЬ БЕЗ РАБОТАЮЩЕГО ПЛЕЕРА
        currentTrack?.let { track ->
            tvPrimaryGenreName.text = track.primaryGenreName
            tvCollectionName.text = track.collectionName
            tvCountry.text = track.country
            tvReleaseDate.text = track.releaseDate.substring(0, 4)
            tvTrackDuration.text = formatTrackTimeMillis(track.trackTimeMillis)
            Glide.with(this)
                .load(getCoverArtwork(track.artworkUrl100))
                .placeholder(R.drawable.placeholder)
                .fitCenter()
                .transform(RoundedCorners(radiusPx))
                .into(ivCollectionImage)
        }

        bPlay.setOnClickListener {
            Toast.makeText(this, "Музыка играет", Toast.LENGTH_SHORT).show()
        }
        bLike.setOnClickListener {
            if (!isPressed_bLike) {
                isPressed_bLike = true
                bLike.setImageResource(R.drawable.ic_like_track_pushed)
            } else {
                isPressed_bLike = false
                bLike.setImageResource(R.drawable.ic_like_track)
            }
        }
        bAddToPlaylist.setOnClickListener {
            if (!isPressed_bAddtoPlaylist) {
                isPressed_bAddtoPlaylist = true
                bAddToPlaylist.setImageResource(R.drawable.ic_add_to_playlist_pushed)
            } else {
                isPressed_bAddtoPlaylist = false
                bAddToPlaylist.setImageResource(R.drawable.ic_add_to_playlist)
            }
        }

    }

    private fun dpToPx(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

    private fun formatTrackTimeMillis(trackTimeMillis: Long): String {
        val minutes = trackTimeMillis / 1000 / 60
        val seconds = (trackTimeMillis / 1000 % 60)
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun getCoverArtwork(link: String): String {
        return link.replaceAfterLast('/', "512x512bb.jpg")
    }
}