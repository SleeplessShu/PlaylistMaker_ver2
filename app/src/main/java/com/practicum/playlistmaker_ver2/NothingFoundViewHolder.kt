package com.practicum.playlistmaker_ver2

import android.view.View
import android.widget.ImageView

import androidx.recyclerview.widget.RecyclerView

class NothingFoundViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageView: ImageView = itemView.findViewById(R.id.ivNothingFound)


    fun bind() {
        val currentNightMode = itemView.context.resources.configuration.uiMode and
                android.content.res.Configuration.UI_MODE_NIGHT_MASK
        if (currentNightMode != android.content.res.Configuration.UI_MODE_NIGHT_YES) {
            imageView.setImageResource(R.drawable.er_nothingfound_l)
        } else {
            imageView.setImageResource(R.drawable.er_nothingfound_d)
        }
    }
}