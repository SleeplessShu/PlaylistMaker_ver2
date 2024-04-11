package com.practicum.playlistmaker_ver2


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val onPressBackToMain = findViewById<TextView>(R.id.buttonBackToMain)
        onPressBackToMain.setOnClickListener {
            finish()
        }
    }
}