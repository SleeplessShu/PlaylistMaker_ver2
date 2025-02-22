package com.practicum.playlistmaker_ver2.playlist.presentation

import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.databinding.PlaylistEditorBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment(

) : Fragment() {
    private val playlistViewModel: PlaylistViewModel by viewModel()
    private var playlistImage: Uri? = null
    private var _binding: PlaylistEditorBinding? = null
    private val binding: PlaylistEditorBinding get() = _binding!!

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                saveImageToPrivateStorage(uri)
            } else {
                Toast.makeText(
                    requireContext(), R.string.playlistImageNotSelected, Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = PlaylistEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.btnBack.setOnClickListener {
            onBackPress()
        }
        setupTextInputWatcher(binding.frameName)
        setupTextInputWatcher(binding.frameDescription)

        binding.imagePlayList.setOnClickListener {
            pickImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.tiPlaylistName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateTextInputLayoutState(binding.frameName)
                checkCreateButtonState()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.tiDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateTextInputLayoutState(binding.frameDescription)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.bCreatePlaylist.setOnClickListener {
            if (binding.bCreatePlaylist.isEnabled) {
                savePlaylist()
            }
        }
    }

    private fun checkCreateButtonState() {
        val isNameNotEmpty = !binding.tiPlaylistName.text.isNullOrBlank()
        binding.bCreatePlaylist.isEnabled = isNameNotEmpty
    }

    private fun setupTextInputWatcher(inputLayout: TextInputLayout) {
        val editText = inputLayout.editText

        editText?.setOnFocusChangeListener { _, _ ->
            updateTextInputLayoutState(inputLayout)
        }
    }

    private fun updateTextInputLayoutState(inputLayout: TextInputLayout) {
        val editText = inputLayout.editText
        val isNotEmpty = !editText?.text.isNullOrBlank()

        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_focused), intArrayOf(android.R.attr.state_enabled)
            ), intArrayOf(
                ContextCompat.getColor(requireContext(), R.color.blue),  // Если в фокусе — синий
                if (isNotEmpty) ContextCompat.getColor(
                    requireContext(), R.color.blue
                )  // Если есть текст — синий
                else ContextCompat.getColor(
                    requireContext(), R.color.playlist_box_stroke
                ) // Стандартный цвет

            )
        )

        inputLayout.setBoxStrokeColorStateList(colorStateList)
    }


    private fun saveImageToPrivateStorage(uri: Uri) {
        val savedUri = playlistViewModel.saveImageToPrivateStorage(uri)
        if (savedUri != null) {
            playlistImage = savedUri
            binding.imagePlayList.setImageURI(playlistImage)
            Toast.makeText(context, R.string.playlistImageSelected, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, R.string.playlistImageSelectionError, Toast.LENGTH_SHORT)
        }


    }

    private fun onBackPress() {
        if (exitCheck()) {
            showPopUpDialogue()
        } else {
            findNavController().popBackStack()
        }
    }

    private fun exitCheck(): Boolean {
        return playlistImage != null
                || !binding.tiPlaylistName.text.isNullOrEmpty()
                || !binding.tiDescription.text.isNullOrEmpty()
    }

    private fun showPopUpDialogue() {
        MaterialAlertDialogBuilder(requireContext()).setTitle(R.string.playlistDialogueHeader)
            .setMessage(R.string.playlistDialogueBody)
            .setNegativeButton(R.string.playlistDialogueNo) { dialog, which -> // Добавляет кнопку «Отмена»
                // Действия, выполняемые при нажатии на кнопку «Отмена»
            }.setPositiveButton(R.string.playlistDialogueYes) { dialog, which ->
                findNavController().popBackStack()
            }.show()
    }


    private fun savePlaylist() {
        val defaultImageUri = Uri.parse("android.resource://${context?.packageName}/${R.drawable.placeholder}")
        val imageUri = playlistImage ?: defaultImageUri
        val title = binding.tiPlaylistName.text.toString()
        val description = binding.tiDescription.text.toString()
        val message = getString(R.string.playlistHasCreated, title)

        playlistViewModel.addPlaylist(imageUri, title, description) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }
}