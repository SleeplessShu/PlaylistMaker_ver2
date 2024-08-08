package com.practicum.playlistmaker_ver2.ui.search

import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker_ver2.databinding.EmptyViewBinding

class ViewHolderEmpty(private val binding: EmptyViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind() {
        // Nothing to bind for empty view
    }
}