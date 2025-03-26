package com.practicum.playlistmaker_ver2.search.presentation.viewHolders

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.databinding.TrackBinding
import com.practicum.playlistmaker_ver2.search.domain.models.Track
import com.practicum.playlistmaker_ver2.utils.DebounceClickListener
import com.practicum.playlistmaker_ver2.utils.formatDpToPx


class ViewHolderTrack(
    private val binding: TrackBinding, private val lifecycleOwner: LifecycleOwner
) : RecyclerView.ViewHolder(binding.root) {

    private var longClickHandled = false

    fun bind(
        track: Track,
        onItemClick: (Track) -> Unit,
        onItemLongClick: ((Track) -> Boolean)? = null
    ) {
        val context = itemView.context
        val radiusPx = context.formatDpToPx(2)
        binding.tvTrackName.text = track.trackName
        binding.tvArtistName.text = track.artistName
        binding.tvDuration.text = track.trackTime

        if (track.artworkUrl100.isNotBlank() && track.artworkUrl100.startsWith("http")) {
            Glide.with(context).load(track.artworkUrl100).placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder_error).fitCenter().transform(RoundedCorners(radiusPx))
                .into(binding.ivCollectionImage)
        } else {
            binding.ivCollectionImage.setImageResource(R.drawable.placeholder_error)
        }
        longClickHandled = false

        itemView.setOnClickListener(DebounceClickListener(lifecycleOwner) {
            if (!longClickHandled) {
                onItemClick(track)
            }
        })
        itemView.setOnLongClickListener {
            longClickHandled = true
            onItemLongClick?.invoke(track) ?: false
        }
    }
}