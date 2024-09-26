package com.practicum.playlistmaker_ver2.mediateka.ui

import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.base.ActivityBase
import com.practicum.playlistmaker_ver2.databinding.ActivityMediatekaBinding
import com.practicum.playlistmaker_ver2.mediateka.adapters.MediatekaPagerAdapter
import com.practicum.playlistmaker_ver2.mediateka.ViewModel.FavoritePlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivityMediateka : ActivityBase() {

    private val viewModel: FavoritePlaylistsViewModel by viewModel()
    private var binding: ActivityMediatekaBinding? = null
    private var tabMediator: TabLayoutMediator? = null
    private var pagerAdapter: MediatekaPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediatekaBinding.inflate(layoutInflater)
        setupStatusBar(androidx.appcompat.R.attr.colorPrimary)
        setContentView(binding?.root)


        pagerAdapter = MediatekaPagerAdapter(this)
        binding?.viewPager?.adapter = pagerAdapter

        tabMediator =
            binding?.tabLayout?.let {
                binding?.viewPager?.let { it1 ->
                    TabLayoutMediator(it, it1) { tab, position ->
                        when (position) {
                            0 -> tab.text = getString(R.string.FavTracks)
                            else -> tab.text = getString(R.string.FavPlaylists)

                        }
                    }
                }
            }
        tabMediator?.attach()

        binding?.bBackToMain?.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator?.detach()
    }
}