package com.practicum.playlistmaker_ver2.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker_ver2.util.Creator
import com.practicum.playlistmaker_ver2.ui.mediateka.ActivityMediateka
import com.practicum.playlistmaker_ver2.ui.search.ActivitySearch
import com.practicum.playlistmaker_ver2.ui.settings.ActivitySettings
import com.practicum.playlistmaker_ver2.util.DebounceClickListener
import com.practicum.playlistmaker_ver2.databinding.ActivityMainBinding
import com.practicum.playlistmaker_ver2.ui.base.ActivityBase

class ActivityMain : ActivityBase() {
    private companion object {
        const val THEME_SHARED_PREFERENCES_KEY = "NightMode"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val appContext = applicationContext
        val sharedPreferences = getSharedPreferences(THEME_SHARED_PREFERENCES_KEY, MODE_PRIVATE)
        val themeInteractor = Creator.provideThemeStatusInteractor(sharedPreferences)

        binding.bSettings.setOnClickListener(DebounceClickListener {
            val intentSettings = Intent(appContext, ActivitySettings::class.java)
            startActivity(intentSettings)
        })
        binding.bMediateka.setOnClickListener(DebounceClickListener {
            val intentMediateka = Intent(appContext, ActivityMediateka::class.java)
            startActivity(intentMediateka)
        })
        binding.bSearch.setOnClickListener(DebounceClickListener {
            val intentSearch = Intent(appContext, ActivitySearch::class.java)
            startActivity(intentSearch)
        })

        val isNightModeOn = themeInteractor.getThemeStatus()
        AppCompatDelegate.setDefaultNightMode(
            if (isNightModeOn) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}
