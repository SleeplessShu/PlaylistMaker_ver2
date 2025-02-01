package com.practicum.playlistmaker_ver2.mediateka.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker_ver2.databinding.FavoritePlaylistsFragmentBinding
import com.practicum.playlistmaker_ver2.player.ui.ActivityPlayer
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavoritePlaylistsFragment : Fragment() {

    private val viewModel: FavoritePlaylistsViewModel by viewModel()
    private var _binding: FavoritePlaylistsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FavoritePlaylistsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUI(){
        binding.bNewPlaylist.setOnClickListener {
            createNewPlaylist(requireContext())
        }
    }

    private fun createNewPlaylist(context: Context){
        val intent = Intent(context, ActivityPlayer::class.java)
        startActivity(intent)
    }

    companion object {
        fun newInstance(): FavoritePlaylistsFragment {
            return FavoritePlaylistsFragment()
        }
    }
}

