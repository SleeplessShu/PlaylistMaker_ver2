package com.practicum.playlistmaker_ver2.ui.search

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker_ver2.util.DebounceClickListener
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.data.dto.TrackData
import com.practicum.playlistmaker_ver2.databinding.TrackBinding
import com.practicum.playlistmaker_ver2.util.formatDpToPx
import com.practicum.playlistmaker_ver2.util.formatLnToStr


class ViewHolderTrack(private val binding: TrackBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(track: TrackData, onItemClick: (TrackData) -> Unit) {
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
        itemView.setOnClickListener(
            DebounceClickListener { onItemClick(track) }
        )
    }
}