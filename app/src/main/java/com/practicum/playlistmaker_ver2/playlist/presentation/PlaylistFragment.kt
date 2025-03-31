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
import com.practicum.playlistmaker_ver2.player.ui.models.MessageState
import com.practicum.playlistmaker_ver2.playlist.presentation.models.TracksInPlaylistState
import com.practicum.playlistmaker_ver2.search.domain.models.Track
import com.practicum.playlistmaker_ver2.search.presentation.adapters.TrackAdapter
import com.practicum.playlistmaker_ver2.utils.PluralUtils
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private var _binding: PlaylistFragmentBinding? = null
    private val binding: PlaylistFragmentBinding get() = _binding!!
    private val playlistViewModel: PlaylistViewModel by viewModel()

    private lateinit var adapter: TrackAdapter
    private lateinit var tracksBottomSheetManager: BottomSheetManager
    private lateinit var optionsBottomSheetManager: BottomSheetManager

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
        playlistViewModel.restoreBottomSheetOptionsState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUI() {
        binding.btnBack.setOnClickListener { onBackPress() }
        binding.icShare.setOnClickListener { playlistViewModel.shareButtonPressed() }
        binding.icMoreOptions.setOnClickListener { playlistViewModel.optionsButtonPressed() }
        binding.playlistShare.setOnClickListener { playlistViewModel.shareButtonPressed() }
        binding.overlay.setOnClickListener { playlistViewModel.bottomSheetOptionsCollapsed() }
        binding.playlistEdit.setOnClickListener {
            editPlaylist()
        }
        binding.playlistDelete.setOnClickListener { showPlaylistDeletingDialogue() }
        setupBottomSheets()
        playlistViewModel.restoreBottomSheetOptionsState()
        setupAdapter()
    }

    private fun onBackPress() {
        findNavController().navigateUp()
    }

    private fun setupObservers() {
        playlistViewModel.playlistData.observe(viewLifecycleOwner) { entity ->
            val tracksCountText =
                PluralUtils.formatTrackCount(requireContext(), entity.playlistEntity.tracksCount)
            updateBottomSheetStates(entity)
            binding.imagePlayList.setImageURI(Uri.parse(entity.playlistEntity.image))
            binding.tvPlaylistName.text = entity.playlistEntity.name
            binding.tvPlaylistDescription.text = entity.playlistEntity.description
            binding.tvPlaylistTracksCount.text = tracksCountText
            binding.tvPlaylistDuration.text = entity.playlistEntity.tracksDuration
            binding.ivPlaylistItemImage.setImageURI(Uri.parse(entity.playlistEntity.image))
            binding.tvPlaylistItemName.text = entity.playlistEntity.name
            binding.tvPlaylistItemTracksCount.text = tracksCountText
            lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    playlistViewModel.toastMessage.collect { message ->
                        when (message) {

                            MessageState.NOTHING_TO_SHARE -> {
                                Toast.makeText(requireContext(), getString(R.string.nothingToShare), Toast.LENGTH_SHORT).show()
                            }
                            else -> Unit
                        }
                    }
                }
            }
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

    private fun updateBottomSheetStates(state: PlaylistPresentationState) {
        binding.overlay.isVisible = state.overlayVisibility
        tracksBottomSheetManager.setState(state.bottomSheetTracks)
        optionsBottomSheetManager.setState(state.bottomSheetOptions)

    }

    private fun setupBottomSheets() {
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels
        tracksBottomSheetManager = BottomSheetManager(
            bottomSheet = binding.tracksBottomSheet,
            initialState = BottomSheetBehavior.STATE_COLLAPSED,
            isHideable = false,
            peekHeightRatio = (screenHeight * 0.33f).toInt()
        )

        optionsBottomSheetManager = BottomSheetManager(
            bottomSheet = binding.optionsBottomSheet,
            overlay = binding.overlay,
            initialState = BottomSheetBehavior.STATE_HIDDEN,
            isHideable = true,
            peekHeightRatio = (screenHeight * 0.48f).toInt()
        )
    }

    private fun setupAdapter() {
        binding.rvTracksList.layoutManager = LinearLayoutManager(requireContext())
        adapter = TrackAdapter(
            emptyList(),
            TrackAdapter.VIEW_TYPE_ITEM,
            null,
            onItemClick = { track -> onTrackClick(track) },
            onItemLongClick = { track ->
                onTrackLongClick(track)
                true
            },
            lifecycleOwner = viewLifecycleOwner
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
            .setNegativeButton(R.string.dialogueNo, null)
            .setPositiveButton(R.string.dialogueYes) { _, _ ->
                playlistViewModel.removeTrackFromPlaylist(trackID)
            }.show()
    }

    private fun showPlaylistDeletingDialogue() {
        val titleString = getString(
            R.string.playlistDialogueDeletePlaylist, binding.tvPlaylistName.text.toString()
        )
        MaterialAlertDialogBuilder(
            requireContext(), R.style.CustomAlertDialogTheme
        ).setTitle(titleString)
            .setNegativeButton(R.string.dialogueNo, null)
            .setPositiveButton(R.string.dialogueYes) { _, _ ->
                deletingPlaylist()
            }.show()
    }

    private fun deletingPlaylist() {
        playlistViewModel.removePlaylistFromDB()
        onBackPress()
    }

    private fun startPlayer(track: Track) {
        val action = PlaylistFragmentDirections.actionPlaylistFragmentToPlayerFragment(
            TrackToPlayerTrackMapper.map(track)
        )
        findNavController().navigate(action)
    }

    private fun editPlaylist() {
        val currentPlaylistEntity =  playlistViewModel.playlistData.value?.playlistEntity ?: return
        val action = PlaylistFragmentDirections.actionPlaylistFragmentToPlaylistCreationFragment(
            playlistToEdit = currentPlaylistEntity
        )
        findNavController().navigate(action)
    }

    companion object {
        fun newInstance(): PlaylistFragment {
            return PlaylistFragment()
        }
    }
}
