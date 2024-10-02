package com.practicum.playlistmaker_ver2.mediateka.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practicum.playlistmaker_ver2.mediateka.presentation.FavoritePlaylistsFragment
import com.practicum.playlistmaker_ver2.mediateka.presentation.FavoriteTracksFragment

class MediatekaPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoriteTracksFragment.newInstance()
            else -> FavoritePlaylistsFragment.newInstance()
        }
    }
}