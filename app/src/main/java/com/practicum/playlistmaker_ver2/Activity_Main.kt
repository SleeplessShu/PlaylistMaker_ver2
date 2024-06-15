package com.practicum.playlistmaker_ver2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate

class Activity_Main : Activity_Base() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onPressSettings = findViewById<Button>(R.id.buttonSettings)
        onPressSettings.setOnClickListener {
            val intentSettings = Intent(this, Activity_Settings::class.java)
            startActivity(intentSettings)
        }
        val onPressMediateka = findViewById<Button>(R.id.buttonMediateka)
        onPressMediateka.setOnClickListener {
            val intentMediateka = Intent(this, Activity_Mediateka::class.java)
            startActivity(intentMediateka)
        }
        val onPressSearch = findViewById<Button>(R.id.buttonSearch)
        onPressSearch.setOnClickListener {
            val intentSearch = Intent(this, Activity_Search::class.java)
            startActivity(intentSearch)
        }
        val prefs = getSharedPreferences("Settings", MODE_PRIVATE)
        val isNightModeOn = prefs.getBoolean("NightMode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isNightModeOn) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}
