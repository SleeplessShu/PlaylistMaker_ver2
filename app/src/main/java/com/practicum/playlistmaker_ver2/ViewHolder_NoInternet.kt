package com.practicum.playlistmaker_ver2

import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ViewHolder_NoInternet(itemView: View, private val onRetry: (() -> Unit)?) :
    RecyclerView.ViewHolder(itemView) {
    private val retryButton: Button = itemView.findViewById(R.id.bRefresh)
    private val imageView: ImageView = itemView.findViewById(R.id.ivNoInternet)

    init {
        retryButton.setOnClickListener {
            onRetry?.invoke()
        }
    }

    fun bind() {
        val currentNightMode = itemView.context.resources.configuration.uiMode and
                android.content.res.Configuration.UI_MODE_NIGHT_MASK
        if (currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
            imageView.setImageResource(R.drawable.er_nointernet_d)
        } else {
            imageView.setImageResource(R.drawable.er_nointernet_l)
        }
    }
}