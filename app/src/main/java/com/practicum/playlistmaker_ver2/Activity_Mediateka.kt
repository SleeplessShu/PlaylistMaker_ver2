package com.practicum.playlistmaker_ver2

import android.os.Bundle
import android.widget.LinearLayout

class Activity_Mediateka : Activity_Base() {
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