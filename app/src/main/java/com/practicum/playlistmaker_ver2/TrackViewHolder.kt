package com.practicum.playlistmaker_ver2

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.compTrackName)
    private val artistName: TextView = itemView.findViewById(R.id.compArtistName)
    private val trackTime: TextView = itemView.findViewById(R.id.compDuration)
    private val artwork: ImageView = itemView.findViewById(R.id.compTrackIcon)
    fun bind(track: TrackData) {
        val context = trackName.context
        val radiusPx = dpToPx(context, 2)
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = lnToStr(track.trackTimeMillis)

        Glide.with(context)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(radiusPx))
            .into(artwork)
    }

    private fun dpToPx(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
    private fun lnToStr(long: Long): String {
        val date = Date(long)
        val format = SimpleDateFormat("mm:ss", Locale.getDefault())
        return format.format(date)
    }
}