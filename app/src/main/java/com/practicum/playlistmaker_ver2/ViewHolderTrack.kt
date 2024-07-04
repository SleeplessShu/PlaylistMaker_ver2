package com.practicum.playlistmaker_ver2

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker_ver2.databinding.TrackBinding


class ViewHolderTrack(private val binding: TrackBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(track: TrackData) {
        val context = itemView.context
        val radiusPx = formatDpToPx(context, 2)
        binding.tvTrackName.text = track.trackName
        binding.tvArtistName.text = track.artistName
        binding.tvDuration.text = formatLnToStr(track.trackTimeMillis)

        Glide.with(context)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(radiusPx))
            .into(binding.ivCollectionImage)
    }
}