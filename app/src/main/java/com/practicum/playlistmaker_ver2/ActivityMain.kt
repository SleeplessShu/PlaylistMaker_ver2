package com.practicum.playlistmaker_ver2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker_ver2.databinding.ActivityMainBinding

class ActivityMain : ActivityBase() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bSettings.setOnClickListener {
            val intentSettings = Intent(this, ActivitySettings::class.java)
            startActivity(intentSettings)
        }
        binding.bMediateka.setOnClickListener {
            val intentMediateka = Intent(this, ActivityMediateka::class.java)
            startActivity(intentMediateka)
        }
        binding.bSearch.setOnClickListener {
            val intentSearch = Intent(this, ActivitySearch::class.java)
            startActivity(intentSearch)
        }
        val prefs = getSharedPreferences("Settings", MODE_PRIVATE)
        val isNightModeOn = prefs.getBoolean("NightMode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isNightModeOn) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}
