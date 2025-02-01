package com.practicum.playlistmaker_ver2.search.presentation.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker_ver2.databinding.ErrorNetworkConnectionBinding

class ViewHolderNoInternet(
    private val binding: ErrorNetworkConnectionBinding,
    private val onRetry: (() -> Unit)?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        binding.bRefresh.setOnClickListener {
            onRetry?.invoke()
        }
    }
}