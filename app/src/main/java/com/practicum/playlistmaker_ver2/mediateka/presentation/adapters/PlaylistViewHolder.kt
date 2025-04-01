package com.practicum.playlistmaker_ver2.mediateka.presentation.adapters

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.playlist_editor.domain.models.PlaylistEntityPresentation
import com.practicum.playlistmaker_ver2.utils.PluralUtils

class PlaylistViewHolder(itemView: View, ) : RecyclerView.ViewHolder(itemView) {
    private val playlistName: TextView = itemView.findViewById(R.id.tvPlaylistItemName)
    private val playlistImage: ImageView = itemView.findViewById(R.id.ivPlaylistItemImage)
    private val playlistTracksCount: TextView =
        itemView.findViewById(R.id.tvPlaylistItemTracksCount)

    fun bind(playlist: PlaylistEntityPresentation, onItemClick: (PlaylistEntityPresentation) -> Unit) {
        playlistImage.setImageURI(Uri.parse(playlist.image))
        playlistName.text = playlist.name
        playlistTracksCount.text = PluralUtils.formatTrackCount(itemView.context, playlist.tracksCount)
        itemView.setOnClickListener { onItemClick(playlist) }
    }
}