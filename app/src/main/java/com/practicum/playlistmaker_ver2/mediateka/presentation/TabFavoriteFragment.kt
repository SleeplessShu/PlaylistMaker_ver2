package com.practicum.playlistmaker_ver2.mediateka.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.databinding.TabFavoriteTracksBinding
import com.practicum.playlistmaker_ver2.mediateka.presentation.states.FavoriteTracksState

import com.practicum.playlistmaker_ver2.player.ui.mappers.TrackToPlayerTrackMapper
import com.practicum.playlistmaker_ver2.search.domain.models.Track
import com.practicum.playlistmaker_ver2.search.presentation.adapters.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class TabFavoriteFragment(private val navController: NavController?) : Fragment() {
    private val viewModel: TabFavoriteViewModel by viewModel()
    private var _binding: TabFavoriteTracksBinding? = null
    private val binding: TabFavoriteTracksBinding get() = _binding!!
    private lateinit var adapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TabFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupUI()

    }

    private fun setupObservers() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }

    private fun setupUI() {
        binding.trackList.layoutManager = LinearLayoutManager(requireContext())
        adapter = TrackAdapter(
            emptyList(),
            TrackAdapter.VIEW_TYPE_ITEM,
            null,
            onItemClick = { track ->
                onTrackClick(track)
            },
            lifecycleOwner = viewLifecycleOwner
        )
        binding.trackList.adapter = adapter

    }

    private fun onTrackClick(track: Track) {
        //viewModel.addToSearchHistory(track)
        startPlayer(track)
    }

    private fun startPlayer(track: Track) {
        val action = MediatekaFragmentDirections.actionFavoriteTracksFragmentToPlayerFragment(
            TrackToPlayerTrackMapper.map(track)
        )
        navController?.navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: FavoriteTracksState) {
        when (state) {
            is FavoriteTracksState.Loading -> showLoading()
            is FavoriteTracksState.Content -> showContent(state.tracks)
            is FavoriteTracksState.Empty -> showEmpty()
        }
    }

    private fun showLoading() {
        binding.progressBar.isVisible = true
        binding.ivNothingFound.isVisible = false
        binding.tvNothingFound.isVisible = false
        binding.scrollable.isVisible = false
    }

    private fun showContent(tracks: List<Track>) {
        adapter?.updateTracks(tracks, TrackAdapter.VIEW_TYPE_ITEM)
        tracks.forEach { track ->
            Log.d("DEBUG", "${track.trackName}, ${track.order}")
        }

        binding.progressBar.isVisible = false
        binding.ivNothingFound.isVisible = false
        binding.tvNothingFound.isVisible = false
        binding.scrollable.isVisible = true
    }

    private fun showEmpty() {
        binding.progressBar.isVisible = false
        binding.ivNothingFound.isVisible = true
        binding.tvNothingFound.isVisible = true
        binding.scrollable.isVisible = false
    }

    companion object {
        fun newInstance(navController: NavController?): TabFavoriteFragment {
            return TabFavoriteFragment(navController)
        }
    }
}
