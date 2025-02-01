package com.practicum.playlistmaker_ver2.playlistCreaton.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker_ver2.databinding.CreatePlaylistFragmentBinding
import com.practicum.playlistmaker_ver2.playlistCreaton.presentation.models.PlaylistViewState
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistCreationFragment: Fragment() {
    private var _binding: CreatePlaylistFragmentBinding? = null
    private val binding: CreatePlaylistFragmentBinding get() = _binding!!
    private val viewModel: PlaylistCreationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): View? {
        _binding = CreatePlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUI() {
        binding.tiPlaylistName.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.bCreatePlaylist.isEnabled = s?.isNotEmpty() == true
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.bCreatePlaylist.setOnClickListener {
            viewModel.createNewPlaylist()
        }
    }

    private fun setupObservers(){
        viewModel.createPlaylistViewState.observe(viewLifecycleOwner) { viewState ->
            when (viewState.state) {
                PlaylistViewState.EMPTY -> {

                }

                PlaylistViewState.EDITED -> {

                }

                PlaylistViewState.CREATED -> {

                }
            }
        }
    }


}