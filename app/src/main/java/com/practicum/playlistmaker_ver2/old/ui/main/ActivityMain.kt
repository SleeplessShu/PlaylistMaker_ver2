package com.practicum.playlistmaker_ver2.old.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker_ver2.creator.Creator
import com.practicum.playlistmaker_ver2.old.ui.mediateka.ActivityMediateka
import com.practicum.playlistmaker_ver2.old.ui.search.ActivitySearch
import com.practicum.playlistmaker_ver2.settings.ui.ActivitySettings
import com.practicum.playlistmaker_ver2.old.util.DebounceClickListener
import com.practicum.playlistmaker_ver2.databinding.ActivityMainBinding
import com.practicum.playlistmaker_ver2.old.ui.base.ActivityBase

class ActivityMain : ActivityBase() {


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val appContext = applicationContext
        val settingsInteractor = Creator.provideSettingsInteractor()


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

        val isNightModeOn = settingsInteractor.getThemeSettings()
        AppCompatDelegate.setDefaultNightMode(
            if (isNightModeOn) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}
