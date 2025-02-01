package com.practicum.playlistmaker_ver2.mediateka.presentation


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker_ver2.databinding.FavoriteTracksBinding
import com.practicum.playlistmaker_ver2.player.ui.ActivityPlayer
import com.practicum.playlistmaker_ver2.player.ui.mappers.TrackToPlayerTrackMapper
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerTrack
import com.practicum.playlistmaker_ver2.search.domain.models.Track
import com.practicum.playlistmaker_ver2.search.presentation.adapters.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavoriteTracksFragment() : Fragment() {
    private val viewModel: FavoriteTracksViewModel by viewModel()
    private var _binding: FavoriteTracksBinding? = null
    private val binding: FavoriteTracksBinding get() = _binding!!
    private lateinit var adapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FavoriteTracksBinding.inflate(inflater, container, false)
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
        startPlayer(requireContext(), track)
    }

    private fun startPlayer(context: Context, track: Track) {
        val playerTrack: PlayerTrack = TrackToPlayerTrackMapper.map(track)
        val intent = Intent(context, ActivityPlayer::class.java).apply {
            putExtra("trackData", playerTrack)
        }
        startActivity(intent)
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
        fun newInstance(): FavoriteTracksFragment {
            return FavoriteTracksFragment()
        }
    }
}
