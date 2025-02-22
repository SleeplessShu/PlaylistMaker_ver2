package com.practicum.playlistmaker_ver2.mediateka.presentation.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practicum.playlistmaker_ver2.mediateka.presentation.TabPlaylistsFragment
import com.practicum.playlistmaker_ver2.mediateka.presentation.TabFavoriteFragment

class MediatekaPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TabFavoriteFragment.newInstance()
            else -> TabPlaylistsFragment.newInstance()
        }
    }
}