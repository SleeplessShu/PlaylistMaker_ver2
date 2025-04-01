package com.practicum.playlistmaker_ver2.search.presentation.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker_ver2.databinding.EmptyViewBinding
import com.practicum.playlistmaker_ver2.databinding.ErrorNetworkConnectionBinding
import com.practicum.playlistmaker_ver2.databinding.ErrorNothingFoundBinding
import com.practicum.playlistmaker_ver2.databinding.TrackBinding
import com.practicum.playlistmaker_ver2.search.domain.models.Track
import com.practicum.playlistmaker_ver2.search.presentation.viewHolders.ViewHolderEmpty
import com.practicum.playlistmaker_ver2.search.presentation.viewHolders.ViewHolderNoInternet
import com.practicum.playlistmaker_ver2.search.presentation.viewHolders.ViewHolderNothingFound
import com.practicum.playlistmaker_ver2.search.presentation.viewHolders.ViewHolderTrack

class TrackAdapter(
    private var trackData: List<Track> = emptyList(),
    private var viewType: Int = VIEW_TYPE_EMPTY,
    private val onRetry: (() -> Unit)? = null,
    private val onItemClick: (Track) -> Unit,
    private val onItemLongClick: ((Track) -> Boolean)? = null,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_EMPTY = -1
        const val VIEW_TYPE_NOTHING_FOUND = 0
        const val VIEW_TYPE_NO_INTERNET = 1
        const val VIEW_TYPE_ITEM = 2
        const val VIEW_TYPE_EMPTY_FAVORITE = 3

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_EMPTY -> {
                val binding =
                    EmptyViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolderEmpty(binding)
            }

            VIEW_TYPE_NOTHING_FOUND -> {
                val binding = ErrorNothingFoundBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ViewHolderNothingFound(binding)
            }

            VIEW_TYPE_NO_INTERNET -> {
                val binding = ErrorNetworkConnectionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ViewHolderNoInternet(binding, onRetry)
            }

            VIEW_TYPE_ITEM -> {
                val binding =
                    TrackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolderTrack(binding, lifecycleOwner)
            }

            else -> {
                val binding = ErrorNetworkConnectionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ViewHolderNoInternet(binding, onRetry)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolderTrack -> if (trackData.isNotEmpty() && position in trackData.indices) {
                holder.bind(trackData[position], onItemClick) { track ->
                    onItemLongClick?.invoke(track) ?: false
                }
            }

            is ViewHolderNothingFound -> holder.bind()
            is ViewHolderNoInternet -> holder.bind()
            is ViewHolderEmpty -> holder.bind()
        }
    }

    override fun getItemCount(): Int {
        return if (trackData.isEmpty()) {
            1
        } else {
            trackData.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (trackData.isEmpty()) {
            viewType
        } else {
            VIEW_TYPE_ITEM
        }
    }

    fun updateTracks(newTracks: List<Track>, newViewType: Int) {
        trackData = newTracks
        viewType = newViewType
        notifyDataSetChanged()
    }
}
