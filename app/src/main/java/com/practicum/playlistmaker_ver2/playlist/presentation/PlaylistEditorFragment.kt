package com.practicum.playlistmaker_ver2.playlist.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.databinding.PlaylistEditorBinding
import com.practicum.playlistmaker_ver2.playlist.data.database.entity.PlaylistEntity
import com.practicum.playlistmaker_ver2.playlist.presentation.models.PlaylistIdState
import com.practicum.playlistmaker_ver2.playlist.presentation.viewmodels.PlaylistCreationViewModel
import com.practicum.playlistmaker_ver2.playlist.presentation.viewmodels.PlaylistInfoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date

class PlaylistEditorFragment : Fragment(){

    companion object {
        const val PLAYLIST_ID = "PLAYLISTID"
        fun newInstance(playListId: Int) = PlaylistEditorFragment().apply {
            arguments = bundleOf(PLAYLIST_ID to playListId)
        }
    }

    private var _binding: PlaylistEditorBinding? = null
    private val viewModel by viewModel<PlaylistInfoViewModel>()
    private val viewModelCreate by viewModel<PlaylistCreationViewModel>()
    var textInputName = ""
    var textDescriptor = ""
    var filePathImage=""


    val binding: PlaylistEditorBinding
        get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = PlaylistEditorBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var playList: PlaylistEntity
        val playListId = requireArguments().getInt(PLAYLIST_ID)

        viewModel.getPlayList(playListId)
        viewModelCreate.checkBottom(binding.NamePlayList.toString())

        viewModel.getPlaylistState().observe(viewLifecycleOwner){ state ->
            when(state){
                is PlaylistIdState.Content -> {
                    playList = state.data
                    filePathImage= state.data.filePath
                    viewModel.setPlayList(playList)
                    binding.tiPlaylistName.setText(playList.namePlayList)
                    binding.tiDescription.setText(playList.description)


                    Glide.with(requireContext())
                        .load(playList.filePath)
                        .placeholder(R.drawable.placeholder)
                        .into(binding.imagePlayList)
                }

                is PlaylistIdState.Error -> Log.d("Sprint 23", "Нет данных")
            }
        }

        viewModelCreate.getBottomState().observe(viewLifecycleOwner){state ->
            when (state) {
                false -> {
                    binding.bCreatePlaylist.isEnabled = false
                }

                true -> {
                    binding.bCreatePlaylist.isEnabled = true
                }
            }
        }
        val textNameListWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {
                textInputName = p0.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                viewModelCreate.checkBottom(textInputName)
            }
        }
        val textDescriptionWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {
                textDescriptor = p0.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                viewModelCreate.checkBottom(textInputName)
            }
        }

        binding.tiPlaylistName.addTextChangedListener(textNameListWatcher)
        binding.tiDescription.addTextChangedListener(textDescriptionWatcher)



        binding.toolbarSearch.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        binding.bCreatePlaylist.setOnClickListener {
            val itemPlayList = viewModel.sharePlayList(playListId)
            viewModel.updatePlayList(itemPlayList,textInputName,textDescriptor,filePathImage)
            findNavController().popBackStack()
        }


        val formatNameImage = SimpleDateFormat("dd.yyyy hh:mm:ss")
        val currentDate = formatNameImage.format(Date())


        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Glide.with(requireContext())
                        .load(uri)
                        .placeholder(R.drawable.placeholder)
                        .transform(CenterCrop(), RoundedCorners(8f.dpToPx(requireContext())))
                        .into(binding.imagePlayList)
                    binding.imagePlayList.setImageURI(uri)
                    saveImageToPrivateStorage(uri, currentDate)
                } else {
                    Log.d("PhotoPicker", "No media file")
                }
            }


        binding.imagePlayList.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            val filePath = File(
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "myalbum"
            )
            val file = File(filePath, binding.imagePlayList.toString())
            binding.imagePlayList.setImageURI(file.toUri())
            filePathImage = filePath.toString().plus("/" + "$currentDate")
            viewModelCreate.saveFilePath(filePath.toString().plus("/" + "$currentDate"))
        }
    }
    fun Float.dpToPx(context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics
        ).toInt()
    }
    private fun saveImageToPrivateStorage(uri: Uri, name: String) {
        val contentResolver = requireActivity().applicationContext.contentResolver
        val filePath =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "${name.toString()}")
        val inputStream = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).isVisible = false
    }
}