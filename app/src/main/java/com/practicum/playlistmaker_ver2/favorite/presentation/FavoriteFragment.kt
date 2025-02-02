package com.practicum.playlistmaker_ver2.favorite.presentation

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.practicum.playlistmaker_ver2.databinding.TabFavoritesFragmentBinding
import com.practicum.playlistmaker_ver2.favorite.presentation.models.FavoriteState
import com.practicum.playlistmaker_ver2.search.domain.models.Track
import com.practicum.playlistmaker_ver2.search.presentation.adapters.TrackAdapter
import com.practicum.playlistmaker_ver2.search.presentation.viewHolders.ViewHolderTrack
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {
    private var _binding : TabFavoritesFragmentBinding? = null
    private val binding : TabFavoritesFragmentBinding
        get() = _binding!!


    private val trackFavorite = ArrayList<Track>()
    private var isClickAllowed = true
    private lateinit var rvFavorite : RecyclerView
    private lateinit var progressBar : ProgressBar
    private lateinit var imageError : ImageView
    private lateinit var textError : TextView
    private val favoriteViewModel by viewModel<FavoriteViewModel>()
    val adapterFavorite = TrackAdapter()

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val KEY = "key"
        fun newInstance() = FavoriteFragment()

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TabFavoritesFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        rvFavorite = binding.rvFavoriteTrack
        progressBar = binding.progressBar
        imageError = binding.iconFavorite
        textError = binding.favoriteTab

        favoriteViewModel.fillData()


        favoriteViewModel.getScreenState().observe(viewLifecycleOwner){ state ->
            when(state){

                is FavoriteState.Error -> {
                    showEmpty()
                }
                is FavoriteState.Content ->{
                    if (state.data.isNotEmpty()) {
                        showFavoriteTrack(state.data)
                    } else showEmpty()
                }
            }
        }


        rvFavorite.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapterFavorite.updateItems(trackFavorite)
        adapterFavorite.notifyDataSetChanged()
        rvFavorite.adapter = adapterFavorite


        adapterFavorite.onItemClickListener = ViewHolderTrack.OnItemClickListener { track ->
            openMedia(track)
            adapterFavorite.updateItems(trackFavorite)
            adapterFavorite.notifyDataSetChanged()
            rvFavorite.adapter = adapterFavorite

        }

    }
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
    fun openMedia(track: Track) {
        val itemMedia = track
        if (clickDebounce()) {
            val mediaIntent = Intent(requireContext(), MediaPlayer::class.java)
            val gson = Gson()
            val json = gson.toJson(itemMedia)
            mediaIntent.putExtra(KEY, json)
            startActivity(mediaIntent)
        }
    }
    private fun clickDebounce():Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }
    private fun showEmpty(){
        textError.isVisible = true
        imageError.isVisible = true
        progressBar.isVisible = false
        rvFavorite.isVisible = false
    }

    private fun showFavoriteTrack(tracks: List<Track>){
        trackFavorite.clear()
        trackFavorite.addAll(tracks)
        adapterFavorite.updateItems(tracks)
        adapterFavorite.notifyDataSetChanged()
        rvFavorite.isVisible = true
        progressBar.isVisible = false
        textError.isVisible = false
        imageError.isVisible = false
    }

    override fun onResume() {
        super.onResume()
        favoriteViewModel.refreshItem()
    }

}