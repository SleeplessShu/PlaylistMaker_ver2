package com.practicum.playlistmaker_ver2.playlist.presentation

import android.Manifest
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
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
import pub.devrel.easypermissions.EasyPermissions

class PlaylistFragment : Fragment(), EasyPermissions.PermissionCallbacks {
    private val playlistViewModel: PlaylistViewModel by viewModel()
    private var _binding: PlaylistEditorBinding? = null
    private val binding: PlaylistEditorBinding get() = _binding!!

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let { playlistViewModel.saveImageToPrivateStorage(it) } ?: Toast.makeText(
                requireContext(), R.string.playlistImageNotSelected, Toast.LENGTH_SHORT
            ).show()
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
        observeViewModel()
    }

    private fun setupUI() {
        setupTextInputWatcher(binding.frameName)
        setupTextInputWatcher(binding.frameDescription)

        binding.btnBack.setOnClickListener { onBackPress() }
        binding.imagePlayList.setOnClickListener {
            val permission =
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    Manifest.permission.READ_MEDIA_IMAGES
                } else {
                    Manifest.permission.READ_EXTERNAL_STORAGE
                }

            if (EasyPermissions.hasPermissions(requireContext(), permission)) {
                pickImageLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            } else {
                EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.appNeedsPermission),
                    REQUEST_CODE_STORAGE,
                    permission
                )
            }
        }




        binding.tiPlaylistName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                playlistViewModel.validatePlaylist(s.toString())
                updateTextInputLayoutState(binding.frameName, s?.isNotEmpty() == true)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.tiDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                playlistViewModel.validateDescription(s.toString())
                updateTextInputLayoutState(binding.frameDescription, s?.isNotEmpty() == true)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.bCreatePlaylist.setOnClickListener {
            playlistViewModel.addPlaylist(
                binding.tiPlaylistName.text.toString(), binding.tiDescription.text.toString()
            ) {
                Toast.makeText(
                    context,
                    getString(R.string.playlistHasCreated, binding.tiPlaylistName.text.toString()),
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackPress()
                }
            })
    }

    private fun observeViewModel() {
        playlistViewModel.playlistImage.observe(viewLifecycleOwner) { uri ->
            binding.imagePlayList.setImageURI(uri)
        }

        playlistViewModel.isPlaylistValid.observe(viewLifecycleOwner) { isValid ->
            binding.bCreatePlaylist.isEnabled = isValid
        }
    }

    private fun onBackPress() {
        if (playlistViewModel.shouldShowExitDialog()) {
            showPopUpDialogue()
        } else {
            findNavController().navigateUp()
        }
    }

    private fun showPopUpDialogue() {
        MaterialAlertDialogBuilder(
            requireContext(),
            R.style.CustomAlertDialogTheme
        ).setTitle(R.string.playlistDialogueHeader).setMessage(R.string.playlistDialogueBody)
            .setNegativeButton(R.string.playlistDialogueNo, null)
            .setPositiveButton(R.string.playlistDialogueYes) { _, _ ->
                findNavController().navigateUp()
            }.show()
    }

    private fun setupTextInputWatcher(inputLayout: TextInputLayout) {
        val editText = inputLayout.editText

        editText?.setOnFocusChangeListener { _, hasFocus ->
            val hasText = !editText.text.isNullOrBlank()
            inputLayout.isSelected = hasText
        }

        editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val hasText = !s.isNullOrBlank()
                inputLayout.isSelected = hasText
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun updateTextInputLayoutState(inputLayout: TextInputLayout, hasFocus: Boolean) {
        val editText = inputLayout.editText
        val hasText = !editText?.text.isNullOrBlank()
        val color = when {
            hasFocus || hasText -> ContextCompat.getColor(requireContext(), R.color.blue)
            else -> ContextCompat.getColor(requireContext(), R.color.grey)
        }

        inputLayout.apply {
            setBoxStrokeColor(color)
            defaultHintTextColor = ColorStateList.valueOf(color)
            isSelected = hasText
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        if (requestCode == REQUEST_CODE_STORAGE) {
            Toast.makeText(
                requireContext(),
                getString(R.string.accessToStorageGranted),
                Toast.LENGTH_SHORT
            )
                .show()
            binding.imagePlayList.performClick()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            Toast.makeText(
                requireContext(),
                getString(R.string.checkSettingsForAccess),
                Toast.LENGTH_LONG
            ).show()
            openAppSettings()
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.permissionDenied),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts(SCHEME, requireContext().packageName, null)
        }
        startActivity(intent)
    }

    private companion object {
        const val REQUEST_CODE_STORAGE = 1001
        const val SCHEME = "package"
    }

}
