package com.practicum.playlistmaker_ver2

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat

class MediatekaActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mediateka)


        setupStatusBar(androidx.appcompat.R.attr.colorPrimary)

        val onPressBackToMain = findViewById<LinearLayout>(R.id.buttonBackToMain)
        onPressBackToMain.setOnClickListener {
            finish()
        }
    }
}