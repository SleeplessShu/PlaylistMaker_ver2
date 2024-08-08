package com.practicum.playlistmaker_ver2.ui.search

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker_ver2.util.Creator
import com.practicum.playlistmaker_ver2.data.dto.TrackDto
import com.practicum.playlistmaker_ver2.databinding.EmptyViewBinding
import com.practicum.playlistmaker_ver2.databinding.ErrorNetworkConnectionBinding
import com.practicum.playlistmaker_ver2.databinding.ErrorNothingFoundBinding
import com.practicum.playlistmaker_ver2.databinding.TrackBinding
import com.practicum.playlistmaker_ver2.domain.models.Track
import com.practicum.playlistmaker_ver2.ui.player.ActivityPlayer
import com.practicum.playlistmaker_ver2.util.Intent.PlayerIntent


class TrackAdapter(
    private val context: Context,
    private var trackData: List<Track> = emptyList(),
    private var viewType: Int = VIEW_TYPE_EMPTY,
    private val onRetry: (() -> Unit)? = null,
    private val onItemClick: (Track) -> Unit

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_EMPTY = -1
        const val VIEW_TYPE_NOTHING_FOUND = 0
        const val VIEW_TYPE_NO_INTERNET = 1
        const val VIEW_TYPE_ITEM = 2
    }

    private var addClickedTrackUseCase = Creator.provideAddClickedTracksUseCase(context)

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
                ViewHolderTrack(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolderTrack -> if (trackData.isNotEmpty()) {
                holder.bind(trackData[position], onItemClick)
                holder.itemView.setOnClickListener {
                    startPlayer(holder.itemView.context, trackData[position])
                    addClickedTrackUseCase.execute(trackData[position])

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
        this.trackData = newTracks
        this.viewType = newViewType
        notifyDataSetChanged()
    }

    private fun startPlayer(context: Context, track: Track) {
        val playerIntent = PlayerIntent.startPlayer(context, track)
        context.startActivity(playerIntent)
    }

}