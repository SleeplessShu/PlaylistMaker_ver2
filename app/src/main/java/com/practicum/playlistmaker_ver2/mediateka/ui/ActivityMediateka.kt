package com.practicum.playlistmaker_ver2.mediateka.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.base.ActivityBase
import com.practicum.playlistmaker_ver2.databinding.ActivityMediatekaBinding
import com.practicum.playlistmaker_ver2.mediateka.MediatekaPagerAdapter
import com.practicum.playlistmaker_ver2.mediateka.ViewModel.FavoritePlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivityMediateka : ActivityBase() {

    private val viewModel: FavoritePlaylistsViewModel by viewModel()
    private lateinit var binding: ActivityMediatekaBinding
    private lateinit var tabMediator: TabLayoutMediator
    private lateinit var pagerAdapter: MediatekaPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediatekaBinding.inflate(layoutInflater)
        setupStatusBar(androidx.appcompat.R.attr.colorPrimary)
        setContentView(binding.root)


        pagerAdapter = MediatekaPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.FavTracks)
                else -> tab.text = getString(R.string.FavPlaylists)

            }
        }
        tabMediator.attach()

        binding.bBackToMain.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}