package com.practicum.playlistmaker_ver2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val reactionOnButtonBackToMain = findViewById<ImageView>(R.id.buttonBackToMain)
        reactionOnButtonBackToMain.setOnClickListener {
            val intentBackToMain = Intent(this, MainActivity::class.java)
            startActivity(intentBackToMain)
        }
    }
}