package com.practicum.playlistmaker_ver2.mediateka.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.playlist.domain.models.PlaylistEntityPresentation
import com.practicum.playlistmaker_ver2.playlist.presentation.LayoutType

class TabPlaylistAdapter(
    private val playlists: List<PlaylistEntityPresentation>,
    private val layoutType: LayoutType,
    private val onItemClick: (PlaylistEntityPresentation) -> Unit,
) :
    RecyclerView.Adapter<PlaylistViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layout = when (layoutType) {
            LayoutType.ForPlaylist -> R.layout.playlist_item
            LayoutType.ForPlayer -> R.layout.playlist_item_player
        }
        val view =
            LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position], onItemClick)
        holder.itemView.setOnClickListener {
            if (position < playlists.size) {
                onItemClick(playlists[position])
            }
        }
    }

    override fun getItemCount() = playlists.size


}