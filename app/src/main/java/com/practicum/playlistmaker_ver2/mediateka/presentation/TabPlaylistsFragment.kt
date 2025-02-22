package com.practicum.playlistmaker_ver2.mediateka.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.databinding.TabPlaylistDataBinding
import com.practicum.playlistmaker_ver2.databinding.TabPlaylistFragmentBinding
import com.practicum.playlistmaker_ver2.databinding.TabPlaylistsEmptyBinding
import com.practicum.playlistmaker_ver2.mediateka.presentation.adapters.TabPlaylistAdapter
import com.practicum.playlistmaker_ver2.playlist.domain.models.PlaylistPresentation
import org.koin.androidx.viewmodel.ext.android.viewModel



class TabPlaylistsFragment : Fragment() {

    private val viewModel: TabPlaylistsViewModel by viewModel()
    private var _binding: TabPlaylistFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = TabPlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("DEBUG", "first")
        viewModel.loadPlaylists()
        viewModel.playlists.observe(viewLifecycleOwner) { playlists ->
            Log.d("DEBUG", "Плейлистов ${playlists.size}")
            if (playlists.isEmpty()) {
                Log.d("DEBUG", "onViewCreated: 1")
                showEmptyView()
            } else {
                Log.d("DEBUG", "onViewCreated: 2")
                showDataView(playlists)

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun showEmptyView() {
        Log.d("DEBUG", "Показан пустой экран")
        binding.viewFlipper.displayedChild = 0
        val emptyBinding = TabPlaylistsEmptyBinding.bind(binding.viewFlipper.getChildAt(0))
        emptyBinding.bNewPlaylist.visibility = View.VISIBLE
        emptyBinding.bNewPlaylist.isClickable = true
        emptyBinding.bNewPlaylist.isFocusable = true
        emptyBinding.bNewPlaylist.setOnClickListener {
            Log.d("DEBUG", "showEmptyView: button pressed")
            findNavController().navigate(R.id.action_playlistFragment_to_playlistCreationFragment)
        }
    }

    private fun showDataView(playlists: List<PlaylistPresentation>) {
        binding.viewFlipper.displayedChild = 1
        val dataBinding = TabPlaylistDataBinding.bind(binding.viewFlipper.getChildAt(1))
        setupRecyclerView(dataBinding, playlists)
        dataBinding.bNewPlaylist.setOnClickListener {
            Log.d("DEBUG", "showEmptyView: button pressed")
            findNavController().navigate(R.id.action_playlistFragment_to_playlistCreationFragment)
        }
    }

    private fun setupRecyclerView(dataBinding: TabPlaylistDataBinding, playlists: List<PlaylistPresentation>) {
        val adapter = TabPlaylistAdapter(playlists)
        dataBinding.rvPlaylistsList.layoutManager = GridLayoutManager(requireContext(), 2)
        dataBinding.rvPlaylistsList.adapter = adapter
    }


    companion object {
        fun newInstance(): TabPlaylistsFragment {
            return TabPlaylistsFragment()
        }
    }
}

