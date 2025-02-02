package com.practicum.playlistmaker_ver2.playlist.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.playlist.data.database.entity.PlaylistTrackEntity
import com.practicum.playlistmaker_ver2.playlist.presentation.holders.PlaylistInfoViewHolder

class PlaylistInfoAdapter(): RecyclerView.Adapter<PlaylistInfoViewHolder> () {

    var onItemClickListener: PlaylistInfoViewHolder.OnItemClickListener? = null
    var OnItemClickLongListener: PlaylistInfoViewHolder.OnItemClickLongListener? = null


    private var items: List<PlaylistTrackEntity> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistInfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track, parent, false)
        return PlaylistInfoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PlaylistInfoViewHolder, position: Int) {
        holder.bind(
            items[position],
            onItemClickListener = onItemClickListener,
            OnItemClickLongListener = OnItemClickLongListener
        )
    }

    fun updateItems(newItems: List<PlaylistTrackEntity>) {
        val oldItems = items
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return oldItems.size
            }

            override fun getNewListSize(): Int {
                return newItems.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldItems[oldItemPosition].trackId == newItems[newItemPosition].trackId
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldItems[oldItemPosition] == newItems[newItemPosition]
            }

        })
        items = newItems.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }
}

