package com.practicum.playlistmaker_ver2

import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker_ver2.databinding.ErrorNetworkConnectionBinding

class ViewHolderNoInternet(
    private val binding: ErrorNetworkConnectionBinding,
    private val onRetry: (() -> Unit)?
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.bRefresh.setOnClickListener {
            onRetry?.invoke()
        }
    }

    fun bind() {
        val currentNightMode = itemView.context.resources.configuration.uiMode and
                android.content.res.Configuration.UI_MODE_NIGHT_MASK
        if (currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
            binding.ivNoInternet.setImageResource(R.drawable.er_nointernet_d)
        } else {
            binding.ivNoInternet.setImageResource(R.drawable.er_nointernet_l)
        }
    }
}