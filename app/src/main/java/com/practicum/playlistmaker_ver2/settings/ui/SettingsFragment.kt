package com.practicum.playlistmaker_ver2.settings.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker_ver2.databinding.SettingsFragmentBinding
import com.practicum.playlistmaker_ver2.mediateka.ui.FavoriteTracksFragment
import com.practicum.playlistmaker_ver2.util.DebounceClickListener
import com.practicum.playlistmaker_ver2.settings.models.ThemeViewState
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {


    private val viewModel: SettingsViewModel by viewModel()
    private var _binding: SettingsFragmentBinding? = null
    private val binding: SettingsFragmentBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()

        viewModel.initializeTheme()
        binding.switcherTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setTheme(isChecked)
        }
        binding.bBackToMain.setOnClickListener(DebounceClickListener {
            parentFragmentManager.popBackStack()
        })/*binding.bBackToMain.setOnClickListener(DebounceClickListener {
            onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    TODO("Not yet implemented")
                }

            })
        })*/

        binding.bMailToSupport.setOnClickListener(DebounceClickListener {
            viewModel.supportSend()
        })

        binding.bShareApp.setOnClickListener(DebounceClickListener {
            viewModel.shareApp()
        })

        binding.bOpenAgreementWeb.setOnClickListener(DebounceClickListener {
            viewModel.openTerm()
        })
    }


    private fun setupObservers() {
        viewModel.getThemeState().observe(viewLifecycleOwner) { state: ThemeViewState ->
            updateUi(state)
        }
    }

    private fun updateUi(state: ThemeViewState) {
        binding.switcherTheme.isChecked = (state.isNightModeOn)
    }

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}