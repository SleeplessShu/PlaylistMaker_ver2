package com.practicum.playlistmaker_ver2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker_ver2.MediatekaActivity
import com.practicum.playlistmaker_ver2.SearchActivity
import com.practicum.playlistmaker_ver2.SettingsActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onPressSettings = findViewById<Button>(R.id.buttonSettings)
        onPressSettings.setOnClickListener {
            val intentSettings = Intent(this, SettingsActivity::class.java)
            startActivity(intentSettings)
        }
        val onPressMediateka = findViewById<Button>(R.id.buttonMediateka)
        onPressMediateka.setOnClickListener {
            val intentMediateka = Intent(this, MediatekaActivity::class.java)
            startActivity(intentMediateka)
        }
        val onPressSearch = findViewById<Button>(R.id.buttonSearch)
        onPressSearch.setOnClickListener {
            val intentSearch = Intent(this, SearchActivity::class.java)
            startActivity(intentSearch)
        }
        val prefs = getSharedPreferences("Settings", MODE_PRIVATE)
        val isNightModeOn = prefs.getBoolean("NightMode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isNightModeOn) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}
