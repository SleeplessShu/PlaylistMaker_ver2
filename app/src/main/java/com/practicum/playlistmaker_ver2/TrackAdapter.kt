package com.practicum.playlistmaker_ver2

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker_ver2.databinding.EmptyViewBinding
import com.practicum.playlistmaker_ver2.databinding.ErrorNetworkConnectionBinding
import com.practicum.playlistmaker_ver2.databinding.ErrorNothingFoundBinding
import com.practicum.playlistmaker_ver2.databinding.TrackBinding

class TrackAdapter(
    private val sharedPreferencesManager: SharedPreferencesManager,
    private var trackData: List<TrackData> = emptyList(),
    private var viewType: Int = VIEW_TYPE_EMPTY,
    private val onRetry: (() -> Unit)? = null,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_EMPTY = -1
        const val VIEW_TYPE_NOTHING_FOUND = 0
        const val VIEW_TYPE_NO_INTERNET = 1
        const val VIEW_TYPE_ITEM = 2
    }

    private val sharedPreferencesKey: String = "clicked_tracks"

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
                holder.bind(trackData[position])
                holder.itemView.setOnClickListener {
                    val context = holder.itemView.context
                    val intentPlayer = Intent(context, ActivityPlayer::class.java)
                    intentPlayer.putExtra("trackData", trackData[position])
                    sharedPreferencesManager.saveData(sharedPreferencesKey, trackData[position])
                    context.startActivity(intentPlayer)
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

    fun updateTracks(newTracks: List<TrackData>, newViewType: Int) {
        this.trackData = newTracks
        this.viewType = newViewType
        notifyDataSetChanged()
    }
}