package com.practicum.playlistmaker_ver2.mediateka.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.playlist.domain.models.PlaylistPresentation

class TabPlaylistAdapter(private val playlists: List<PlaylistPresentation>) :
    RecyclerView.Adapter<PlaylistViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    override fun getItemCount() = playlists.size


}