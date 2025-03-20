package com.practicum.playlistmaker_ver2.playlist.presentation

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker_ver2.databinding.PlaylistFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private var _binding: PlaylistFragmentBinding? = null
    private val binding: PlaylistFragmentBinding get() = _binding!!
    private val playlistViewModel: PlaylistViewModel by viewModel()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>


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
        bottomSheetBehavior = BottomSheetBehavior.from(
            binding.tracksBottomSheet
        ).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }
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
                context, "sharing", Toast.LENGTH_SHORT
            ).show()
        }
        setupBottomSheetPeekHeight()
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
        }
       /* bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> Log.d("DEBUG", "onStateChanged: ${newState}")
                    BottomSheetBehavior.STATE_EXPANDED -> Log.d("DEBUG", "onStateChanged: ${newState}")
                    else -> Log.d("DEBUG", "onStateChanged: ${newState}")
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })*/
    }

    private fun setupBottomSheetPeekHeight() {
        val displayMetrics = resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels
        val peekHeightPercentage = 0.33
        val calculatedPeekHeight = (screenHeight * peekHeightPercentage).toInt()
        bottomSheetBehavior.maxHeight = calculatedPeekHeight
    }

    companion object {
        fun newInstance(): PlaylistFragment {
            return PlaylistFragment()
        }
    }

}