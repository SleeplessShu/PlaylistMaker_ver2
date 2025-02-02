package com.practicum.playlistmaker_ver2.mediateka.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.databinding.MediatekaFragmentBinding
import com.practicum.playlistmaker_ver2.mediateka.adapters.MediatekaPagerAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediatekaFragment : Fragment() {

    //private val viewModel: FavoritePlaylistsViewModel by viewModel()
    private var _binding: MediatekaFragmentBinding? = null
    private val binding: MediatekaFragmentBinding get() = _binding!!

    private var tabMediator: TabLayoutMediator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = MediatekaFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        tabMediator?.detach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pagerAdapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        binding.viewPager.adapter = pagerAdapter



        tabMediator = binding?.tabLayout?.let {
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
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
        tabMediator?.detach()
    }
}