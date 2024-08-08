package com.practicum.playlistmaker_ver2.ui.search

import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.databinding.ErrorNothingFoundBinding

class ViewHolderNothingFound(private val binding: ErrorNothingFoundBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        val currentNightMode = itemView.context.resources.configuration.uiMode and
                android.content.res.Configuration.UI_MODE_NIGHT_MASK
        if (currentNightMode != android.content.res.Configuration.UI_MODE_NIGHT_YES) {
            binding.ivNothingFound.setImageResource(R.drawable.er_nothingfound_l)
        } else {
            binding.ivNothingFound.setImageResource(R.drawable.er_nothingfound_d)
        }
    }
}