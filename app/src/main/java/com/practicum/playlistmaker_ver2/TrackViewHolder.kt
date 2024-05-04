package com.practicum.playlistmaker_ver2

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners



class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.compTrackName)
    private val artistName: TextView = itemView.findViewById(R.id.compArtistName)
    private val trackTime: TextView = itemView.findViewById(R.id.compDuration)
    private val artworkUrl100: ImageView = itemView.findViewById(R.id.compTrackIcon)
    fun bind(model: TrackData) {
        val context = trackName.context
        val radiusPx = dpToPx(context, 2)
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTime

        Glide.with(context)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(radiusPx))
            .into(artworkUrl100)
    }

    private fun dpToPx(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}