package com.practicum.playlistmaker_ver2.playlist.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.databinding.TabMediafragmentBinding
import com.practicum.playlistmaker_ver2.playlist.data.database.entity.PlaylistEntity
import com.practicum.playlistmaker_ver2.playlist.presentation.adapters.PlaylistAdapter
import com.practicum.playlistmaker_ver2.playlist.presentation.holders.PlaylistViewHolder
import com.practicum.playlistmaker_ver2.playlist.presentation.models.PlaylistState
import com.practicum.playlistmaker_ver2.playlist.presentation.viewmodels.CreatePlaylistViewModel
import com.practicum.playlistmaker_ver2.playlist.presentation.viewmodels.PlaylistFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {
    companion object {
        fun newInstance() = PlaylistFragment()
    }
    private var _binding: TabMediafragmentBinding? = null
    lateinit var rvPlayList: RecyclerView
    lateinit var playListAdapter: PlaylistAdapter


    private val playList = ArrayList<PlaylistEntity>()


    private val viewModelCreatePlayList by viewModel<CreatePlaylistViewModel>()
    private val viewModelPlayList by viewModel<PlaylistFragmentViewModel>()
    val binding: TabMediafragmentBinding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = TabMediafragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        val createPlaylist = binding.createPlayList
        rvPlayList = binding.rvPlayList
        rvPlayList.layoutManager = GridLayoutManager(requireContext(),2)

        viewModelCreatePlayList.getPlayList()

        viewModelCreatePlayList.getPlayListState().observe(viewLifecycleOwner){ state->
            when(state){
                is PlaylistState.Error -> {
                    showEmpty()
                }
                is PlaylistState.Content ->{
                    showPlayList()
                    playListAdapter = PlaylistAdapter(state.data)
                    rvPlayList.adapter = playListAdapter
                    playListAdapter.notifyDataSetChanged()
                    playListAdapter.onItemClickListener = PlaylistViewHolder.OnItemClickListener{ track->
                        findNavController().navigate(
                            R.id.action_mediaFragment_to_playlistInfoFragment,
                            Bundle().apply {
                            putInt("PLAYLIST",track.playListId)
                        })

                    }
                }
            }
        }


        createPlaylist.setOnClickListener {
            it.findNavController().navigate(R.id.PlaylistCreationFragment)
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
    private fun showEmpty(){
        binding.iconMedia.isVisible = true
        binding.mediaTab.isVisible = true
        rvPlayList.isVisible = false
    }

    private fun showPlayList(){
        rvPlayList.isVisible = true
        binding.iconMedia.isVisible = false
        binding.mediaTab.isVisible = false
    }


}