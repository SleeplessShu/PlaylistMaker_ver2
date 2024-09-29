package com.practicum.playlistmaker_ver2.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.practicum.playlistmaker_ver2.App.Companion.appContext
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.mediateka.ui.MediatekaFragment
import com.practicum.playlistmaker_ver2.search.ui.SearchFragment
import com.practicum.playlistmaker_ver2.settings.ui.SettingsFragment
import com.practicum.playlistmaker_ver2.util.DebounceClickListener
import com.practicum.playlistmaker_ver2.databinding.MenuFragmentBinding
import com.practicum.playlistmaker_ver2.settings.domain.api.SettingsInteractor
import org.koin.java.KoinJavaComponent.getKoin

class MenuFragment : Fragment() {


    private var _binding: MenuFragmentBinding? = null
    private val binding: MenuFragmentBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = MenuFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val settingsInteractor: SettingsInteractor = getKoin().get()

        binding.bSettings.setOnClickListener(DebounceClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragment_container, SettingsFragment.newInstance())
                addToBackStack(null)
            }
        })


        binding.bMediateka.setOnClickListener(DebounceClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragment_container, MediatekaFragment.newInstance())
                addToBackStack(null)
            }
        })


        binding.bSearch.setOnClickListener(DebounceClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragment_container, SearchFragment.newInstance())
                addToBackStack(null)
            }
        })

        val isNightModeOn = settingsInteractor.getThemeSettings()
        AppCompatDelegate.setDefaultNightMode(
            if (isNightModeOn) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
