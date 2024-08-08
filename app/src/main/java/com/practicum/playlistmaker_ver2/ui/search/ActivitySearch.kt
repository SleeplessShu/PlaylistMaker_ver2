package com.practicum.playlistmaker_ver2.ui.search

import android.os.Bundle
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.util.Creator
import com.practicum.playlistmaker_ver2.databinding.ActivitySearchBinding
import com.practicum.playlistmaker_ver2.presentation.SearchController
import com.practicum.playlistmaker_ver2.ui.base.ActivityBase


class ActivitySearch : ActivityBase() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var trackAdapter: TrackAdapter

    private val tracksSearchController by lazy {
        Creator.provideTrackSearchController(
            activity = this,
            binding = binding
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tracksSearchController.onCreate(savedInstanceState)
        setupStatusBar(androidx.appcompat.R.attr.colorPrimary)
        savedInstanceState?.getString(SearchController.SEARCH_TEXT_KEY)?.let {
            tracksSearchController.updateButtonVisibility(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tracksSearchController.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tracksSearchController.onSaveInstanceState(outState)
    }


}
