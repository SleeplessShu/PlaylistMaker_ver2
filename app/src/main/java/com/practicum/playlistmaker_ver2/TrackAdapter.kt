package com.practicum.playlistmaker_ver2

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

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
                val view: View =
                    LayoutInflater.from(parent.context).inflate(R.layout.empty_view, parent, false)
                EmptyViewHolder(view)
            }

            VIEW_TYPE_NOTHING_FOUND -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.error_nothing_found, parent, false)
                NothingFoundViewHolder(view)
            }

            VIEW_TYPE_NO_INTERNET -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.error_network_connection, parent, false)
                NoInternetViewHolder(view, onRetry)
            }

            VIEW_TYPE_ITEM -> {
                val view: View =
                    LayoutInflater.from(parent.context).inflate(R.layout.track, parent, false)
                TrackViewHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TrackViewHolder ->
                if (trackData.isNotEmpty()) {
                    holder.bind(trackData[position])
                    holder.itemView.setOnClickListener {

                        sharedPreferencesManager.saveData(sharedPreferencesKey, trackData[position])

                    }
                }

            is NothingFoundViewHolder -> holder.bind()
            is NoInternetViewHolder -> holder.bind()
            is EmptyViewHolder -> holder.bind()
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