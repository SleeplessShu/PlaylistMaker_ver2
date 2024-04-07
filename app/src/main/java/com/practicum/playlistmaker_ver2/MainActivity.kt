package com.practicum.playlistmaker_ver2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.practicum.playlistmaker_ver2.MediatekaActivity
import com.practicum.playlistmaker_ver2.SearchActivity
import com.practicum.playlistmaker_ver2.SettingsActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val reactionOnButtonSettings = findViewById<Button>(R.id.buttonSettings)
        reactionOnButtonSettings.setOnClickListener {
            val intentSettings = Intent(this, SettingsActivity::class.java)
            startActivity(intentSettings)
        }
        val reactionOnButtonMediateka = findViewById<Button>(R.id.buttonMediateka)
        reactionOnButtonMediateka.setOnClickListener {
            val intentMediateka = Intent(this, MediatekaActivity::class.java)
            startActivity(intentMediateka)
        }
        val reactionOnButtonSearch = findViewById<Button>(R.id.buttonSearch)
        reactionOnButtonSearch.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intentButtonSearch = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intentButtonSearch)
            }
        })


    }
}