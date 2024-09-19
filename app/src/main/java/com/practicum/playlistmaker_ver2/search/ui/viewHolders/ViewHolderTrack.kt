package com.practicum.playlistmaker_ver2.search.ui.viewHolders

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker_ver2.util.DebounceClickListener
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.databinding.TrackBinding
import com.practicum.playlistmaker_ver2.search.domain.models.Track
import com.practicum.playlistmaker_ver2.util.formatDpToPx


class ViewHolderTrack(private val binding: TrackBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(track: Track, onItemClick: (Track) -> Unit) {
        val context = itemView.context
        val radiusPx = context.formatDpToPx(2)
        binding.tvTrackName.text = track.trackName
        binding.tvArtistName.text = track.artistName
        binding.tvDuration.text = track.trackTime

        if (track.artworkUrl100.isNotBlank() && track.artworkUrl100.startsWith("http")) {
        Glide.with(context)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder_error)
            .fitCenter()
            .transform(RoundedCorners(radiusPx))
            .into(binding.ivCollectionImage)
        } else {
            Log.d("GlideDebug", "Loading image from URL: ${track.artworkUrl100}")
            binding.ivCollectionImage.setImageResource(R.drawable.placeholder_error)
        }
        itemView.setOnClickListener(
            DebounceClickListener { onItemClick(track) }
        )
    }
}