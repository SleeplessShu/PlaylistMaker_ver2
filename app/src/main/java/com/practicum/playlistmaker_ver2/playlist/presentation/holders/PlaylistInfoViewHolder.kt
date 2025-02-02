package com.practicum.playlistmaker_ver2.playlist.presentation.holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.playlist.data.database.entity.PlaylistTrackEntity
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistInfoViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
    private val tvTrackName: TextView = itemView.findViewById(R.id.tvTrackName)
    private val tvArtistName: TextView = itemView.findViewById(R.id.tv_artist_name)
    private val tvTrackTimeMillis: TextView = itemView.findViewById(R.id.tvTrackDuration)
    private val ivArtworkUrl100: ImageView = itemView.findViewById(R.id.ivCollectionImage)
    fun bind(
        item: PlaylistTrackEntity,
        onItemClickListener: OnItemClickListener?,
        OnItemClickLongListener: OnItemClickLongListener?
    ) {
        tvTrackName.text = item.trackName
        tvArtistName.text = item.artistName
        tvTrackTimeMillis.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)

        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.rounded_corner_radius)))
            .into(ivArtworkUrl100)

        itemView.setOnClickListener {
            onItemClickListener?.onItemClick(item)
        }
        itemView.setOnLongClickListener{
            OnItemClickLongListener?.onItemClick(item)
            true
        }

    }
    fun interface OnItemClickListener {
        fun onItemClick(item: PlaylistTrackEntity)

    }
    fun interface OnItemClickLongListener {
        fun onItemClick(item: PlaylistTrackEntity)

    }

}