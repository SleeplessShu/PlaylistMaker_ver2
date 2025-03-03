package com.practicum.playlistmaker_ver2.mediateka.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.databinding.TabPlaylistDataBinding
import com.practicum.playlistmaker_ver2.databinding.TabPlaylistFragmentBinding
import com.practicum.playlistmaker_ver2.databinding.TabPlaylistsEmptyBinding
import com.practicum.playlistmaker_ver2.mediateka.presentation.adapters.TabPlaylistAdapter
import com.practicum.playlistmaker_ver2.playlist.domain.models.PlaylistEntityPresentation
import com.practicum.playlistmaker_ver2.playlist.presentation.LayoutType
import org.koin.androidx.viewmodel.ext.android.viewModel


class TabPlaylistsFragment(private val navController: NavController?) : Fragment() {

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
        viewModel.loadPlaylists()
        viewModel.playlists.observe(viewLifecycleOwner) { playlists ->
            if (playlists.isEmpty()) {
                showEmptyView()
            } else {
                showDataView(playlists)

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun showEmptyView() {
        binding.viewFlipper.displayedChild = 0
        val emptyBinding = TabPlaylistsEmptyBinding.bind(binding.viewFlipper.getChildAt(0))
        emptyBinding.bNewPlaylist.visibility = View.VISIBLE
        emptyBinding.bNewPlaylist.isClickable = true
        emptyBinding.bNewPlaylist.isFocusable = true
        emptyBinding.bNewPlaylist.setOnClickListener {
            navController?.navigate(R.id.action_mediatekaFragment_to_playlistCreationFragment)


        }
    }

    private fun showDataView(playlists: List<PlaylistEntityPresentation>) {
        binding.viewFlipper.displayedChild = 1
        val dataBinding = TabPlaylistDataBinding.bind(binding.viewFlipper.getChildAt(1))
        setupRecyclerView(dataBinding, playlists)
        dataBinding.bNewPlaylist.setOnClickListener {
            navController?.navigate(R.id.action_mediatekaFragment_to_playlistCreationFragment)


        }
    }

    private fun setupRecyclerView(
        dataBinding: TabPlaylistDataBinding, playlists: List<PlaylistEntityPresentation>
    ) {
        val adapter = TabPlaylistAdapter(playlists = playlists,
            layoutType = LayoutType.ForPlaylist,
            onItemClick = { playlist ->
                onPlaylistClick(playlist)
            })
        dataBinding.rvPlaylistsList.layoutManager = GridLayoutManager(requireContext(), 2)
        dataBinding.rvPlaylistsList.adapter = adapter
    }

    private fun onPlaylistClick(playlist: PlaylistEntityPresentation) {

        Toast.makeText(context, "clicked on playlist ${playlist}", Toast.LENGTH_SHORT).show()
        //viewModel.onPlaylistClick(playlist)
    }

    companion object {
        fun newInstance(navController: NavController?): TabPlaylistsFragment {
            return TabPlaylistsFragment(navController)
        }
    }
}

