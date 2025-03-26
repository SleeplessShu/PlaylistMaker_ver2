package com.practicum.playlistmaker_ver2.playlist.presentation

import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.databinding.PlaylistFragmentBinding
import com.practicum.playlistmaker_ver2.player.ui.mappers.TrackToPlayerTrackMapper
import com.practicum.playlistmaker_ver2.playlist.presentation.models.TracksInPlaylistState
import com.practicum.playlistmaker_ver2.search.domain.models.Track
import com.practicum.playlistmaker_ver2.search.presentation.adapters.TrackAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private var _binding: PlaylistFragmentBinding? = null
    private val binding: PlaylistFragmentBinding get() = _binding!!
    private val playlistViewModel: PlaylistViewModel by viewModel()

    //private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var adapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = PlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: PlaylistFragmentArgs by navArgs()
        playlistViewModel.getPlaylistByID(args.playlistID)
        setupUI()
        setupObservers()
        playlistViewModel.restoreBottomSheetState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUI() {
        binding.btnBack.setOnClickListener { onBackPress() }
        binding.icShare.setOnClickListener {
            Toast.makeText(context, "sharing", Toast.LENGTH_SHORT).show()
        }
        binding.icMoreOptions.setOnClickListener {
            Toast.makeText(
                context, "options", Toast.LENGTH_SHORT
            ).show()
        }
        setupBottomSheet()
        setupAdapter()
    }

    private fun onBackPress() {
        findNavController().navigateUp()
    }

    private fun setupObservers() {
        playlistViewModel.playlistData.observe(viewLifecycleOwner) { entity ->
            binding.imagePlayList.setImageURI(Uri.parse(entity.playlistEntity.image))
            binding.tvPlaylistName.text = entity.playlistEntity.name
            binding.tvPlaylistDescription.text = entity.playlistEntity.description
            binding.tvPlaylistTracksCount.text = entity.playlistEntity.tracksCount.toString()
            binding.tvPlaylistDuration.text = entity.playlistEntity.tracksDuration
            lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    playlistViewModel.tracksData.collect { state ->
                        render(state)
                    }
                }
            }
        }
    }

    private fun render(trackData: TracksInPlaylistState) {
        val isEmpty = trackData is TracksInPlaylistState.Empty
        binding.ivNothingFound.isVisible = isEmpty
        binding.tvNothingFound.isVisible = isEmpty
        binding.rvTracksList.isVisible = !isEmpty
        if (!isEmpty && trackData is TracksInPlaylistState.Content) {
            adapter.updateTracks(trackData.tracks, TrackAdapter.VIEW_TYPE_ITEM)
        }
    }


    private fun setupBottomSheet() {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.tracksBottomSheet)
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels
        bottomSheetBehavior.peekHeight = (screenHeight * 0.33f).toInt()
        bottomSheetBehavior.isHideable = false
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
        })
    }

    private fun setupAdapter() {
        binding.rvTracksList.layoutManager = LinearLayoutManager(requireContext())
        adapter =
            TrackAdapter(emptyList(), TrackAdapter.VIEW_TYPE_ITEM, null, onItemClick = { track ->
                onTrackClick(track)
            }, onItemLongClick = { track ->
                onTrackLongClick(track)
                true
            }, lifecycleOwner = viewLifecycleOwner
            )
        binding.rvTracksList.adapter = adapter
    }

    private fun onTrackClick(track: Track) {
        startPlayer(track)
    }

    private fun onTrackLongClick(track: Track) {
        showTrackDeletingDialogue(track.trackId)
    }

    private fun showTrackDeletingDialogue(trackID: Int) {
        MaterialAlertDialogBuilder(
            requireContext(), R.style.CustomAlertDialogTheme
        ).setTitle(R.string.playlistDialogueDeleteTrack)
            .setNegativeButton(R.string.playlistDialogueNo, null)
            .setPositiveButton(R.string.playlistDialogueYes) { _, _ ->
                playlistViewModel.removeTrackFromPlaylist(trackID)
            }.show()
    }

    private fun startPlayer(track: Track) {
        val action = PlaylistFragmentDirections.actionPlaylistFragmentToPlayerFragment(
            TrackToPlayerTrackMapper.map(track)
        )
        findNavController().navigate(action)
    }


    companion object {
        fun newInstance(): PlaylistFragment {
            return PlaylistFragment()
        }
    }

}