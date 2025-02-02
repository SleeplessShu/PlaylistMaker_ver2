package com.practicum.playlistmaker_ver2.playlist.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.playlist.data.database.entity.PlaylistEntity
import com.practicum.playlistmaker_ver2.playlist.presentation.holders.PlaylistViewHolder

class PlaylistAdapter (private val playlist:List<PlaylistEntity>): RecyclerView.Adapter<PlaylistViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent, false)
        return PlaylistViewHolder(view)
    }
    override fun getItemCount(): Int {
        return playlist.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlist[position],onItemClickListener = onItemClickListener)
    }
    var onItemClickListener: PlaylistViewHolder.OnItemClickListener? = null

}