package com.practicum.playlistmaker_ver2.player.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.databinding.PlayerFragmentBinding
import com.practicum.playlistmaker_ver2.mediateka.presentation.adapters.TabPlaylistAdapter
import com.practicum.playlistmaker_ver2.player.ui.models.MessageState
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerState
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerTrack
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerViewState
import com.practicum.playlistmaker_ver2.player.ui.models.UiState
import com.practicum.playlistmaker_ver2.playlist.domain.models.PlaylistEntityPresentation
import com.practicum.playlistmaker_ver2.playlist.presentation.LayoutType
import com.practicum.playlistmaker_ver2.utils.formatDpToPx
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : Fragment() {
    private lateinit var binding: PlayerFragmentBinding
    private val viewModel: PlayerViewModel by viewModel()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var currentTrack: PlayerTrack


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = PlayerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: PlayerFragmentArgs by navArgs()
        currentTrack = args.track
        val bottomSheetContainer = binding.addPlaylistBottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        setupUI()
        setupObservers()
        currentTrack.let { track ->
            viewModel.setupPlayer(track.previewUrl)
            initializeViews(track)
        }
        setupBottomSheetPeekHeight()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.releasePlayer()
    }

    private fun initializeViews(currentTrack: PlayerTrack) {
        binding.apply {
            tvTrackName.text = currentTrack.trackName
            tvArtistName.text = currentTrack.artistName
            tvPrimaryGenreName.text = currentTrack.primaryGenreName
            tvCollectionName.text = currentTrack.collectionName
            tvCountry.text = currentTrack.country
            tvReleaseDate.text = currentTrack.releaseDate
            tvTrackDuration.text = currentTrack.trackTime
            binding.tvPlayTime.text = getString(R.string.defaultTime)
            lifecycleScope.launch {
                viewModel.checkInLiked(currentTrack)
            }
        }
        val radiusPx = requireContext().formatDpToPx(8)
        Glide.with(this).load(currentTrack.artworkUrl500).placeholder(R.drawable.placeholder)
            .fitCenter().transform(RoundedCorners(radiusPx)).into(binding.ivCollectionImage)
    }

    private fun setupUI() {
        binding.apply {
            binding.bBack.setOnClickListener { onBackPressed() }
            binding.bLike.setOnClickListener { viewModel.reactOnLikeButton(currentTrack) }
            binding.bAddToPlaylist.setOnClickListener { viewModel.reactOnPlaylistButton(currentTrack.trackId) }
            binding.bPlay.setOnClickListener { viewModel.playPause() }
            binding.bNewPlaylist.setOnClickListener {
                findNavController().navigate(R.id.action_playerFragment_to_playlistCreationFragment)
            }
        }
    }

    private fun setupObservers() {
        Log.d("DEBUG", "fragment onViewCreated: ${currentTrack}")
        viewModel.viewState.observe(viewLifecycleOwner) { updatePlayer(it) }
        viewModel.uiState.observe(viewLifecycleOwner) { updateUI(it) }
        viewModel.playlists.observe(viewLifecycleOwner) { playlists ->
            setupRecyclerView(playlists)
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        viewModel.bottomSheetCollapsed()
                    }

                    else -> {}
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

                binding.overlay.animate().alpha(slideOffset).setDuration(300).start()

            }
        })
    }

    private fun updatePlayer(viewState: PlayerViewState) {
        binding.tvPlayTime.text = viewState.currentTime
        when (viewState.playerState) {
            PlayerState.DEFAULT -> {
                binding.bPlay.isEnabled = false
            }

            PlayerState.PREPARED, PlayerState.PAUSED, PlayerState.STOPPED -> {
                binding.bPlay.setImageResource(R.drawable.ic_play)
                binding.bPlay.isEnabled = true
            }

            PlayerState.PLAYING -> {
                binding.bPlay.setImageResource(R.drawable.ic_pause)
            }

            PlayerState.ERROR -> {
                binding.bPlay.setImageResource(R.drawable.ic_play)
                Toast.makeText(context, viewState.errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateUI(uiState: UiState) {
        binding.bAddToPlaylist.setImageResource(
            if (uiState.inPlaylist) R.drawable.ic_add_to_playlist_pushed else R.drawable.ic_add_to_playlist
        )
        binding.bLike.setImageResource(
            if (uiState.isLiked) R.drawable.ic_like_track_pushed else R.drawable.ic_like_track
        )
        binding.overlay.isVisible = uiState.overlayVisibility
        if (uiState.bottomSheet in listOf(
                BottomSheetBehavior.STATE_EXPANDED,
                BottomSheetBehavior.STATE_COLLAPSED,
                BottomSheetBehavior.STATE_HIDDEN,
                BottomSheetBehavior.STATE_HALF_EXPANDED
            )
        ) {
            BottomSheetBehavior.from(binding.addPlaylistBottomSheet).state = uiState.bottomSheet
        }
        when (uiState.messageState) {
            MessageState.NOTHING -> {}
            MessageState.FAIL -> Toast.makeText(
                context, getString(R.string.addInPlaylistFail), Toast.LENGTH_SHORT
            ).show()

            MessageState.SUCCESS -> Toast.makeText(
                context, getString(R.string.addInPlaylistSuccess), Toast.LENGTH_SHORT
            ).show()

            MessageState.ALREADY_ADDED

            -> Toast.makeText(
                context, getString(R.string.addInPlaylistAlreadyAdded), Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setupRecyclerView(
        playlists: List<PlaylistEntityPresentation>
    ) {
        binding.rvPlaylistsList.layoutManager = LinearLayoutManager(context)
        binding.rvPlaylistsList.adapter = TabPlaylistAdapter(
            playlists = playlists,
            layoutType = LayoutType.ForPlayer,
            onItemClick = { playlist ->
                onPlaylistClick(playlist)
            })
    }

    private fun setupBottomSheetPeekHeight() {
        val displayMetrics = resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels
        val peekHeightPercentage = 0.63
        val calculatedPeekHeight = (screenHeight * peekHeightPercentage).toInt()

        bottomSheetBehavior.maxHeight = calculatedPeekHeight
    }

    private fun onPlaylistClick(playlist: PlaylistEntityPresentation) {
        viewModel.onPlaylistClick(playlist, currentTrack)
    }

    private fun onBackPressed() {
        findNavController().popBackStack()
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}