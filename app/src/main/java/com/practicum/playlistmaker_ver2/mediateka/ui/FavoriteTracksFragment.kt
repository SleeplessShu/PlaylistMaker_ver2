package com.practicum.playlistmaker_ver2.mediateka.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker_ver2.databinding.FavoriteTracksFragmentBinding
import com.practicum.playlistmaker_ver2.mediateka.ViewModel.FavoriteTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavoriteTracksFragment : Fragment() {
    private val viewModel: FavoriteTracksViewModel by viewModel()
    private var _binding: FavoriteTracksFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoriteTracksFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
