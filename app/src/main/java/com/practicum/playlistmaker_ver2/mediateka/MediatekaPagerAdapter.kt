package com.practicum.playlistmaker_ver2.mediateka

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practicum.playlistmaker_ver2.mediateka.ui.FavoritePlaylistsFragment
import com.practicum.playlistmaker_ver2.mediateka.ui.FavoriteTracksFragment

class MediatekaPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoriteTracksFragment()
            else -> FavoritePlaylistsFragment()
        }
    }
}