package com.practicum.playlistmaker_ver2.mediateka.ui

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
import com.practicum.playlistmaker_ver2.mediateka.presentation.FavoritePlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediatekaFragment : Fragment() {

    private val viewModel: FavoritePlaylistsViewModel by viewModel()
    private var _binding: MediatekaFragmentBinding? = null
    private val binding: MediatekaFragmentBinding get() = _binding!!

    private var tabMediator: TabLayoutMediator? = null
    private var pagerAdapter: MediatekaPagerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = MediatekaFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        //parentFragmentManager.popBackStack()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pagerAdapter = MediatekaPagerAdapter(requireActivity() as AppCompatActivity)
        binding?.viewPager?.adapter = pagerAdapter

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
        super.onDestroy()
        tabMediator?.detach()
    }
}