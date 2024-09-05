package com.practicum.playlistmaker_ver2.old.ui.mediateka

import android.os.Bundle
import com.practicum.playlistmaker_ver2.util.DebounceClickListener
import com.practicum.playlistmaker_ver2.databinding.ActivityMediatekaBinding
import com.practicum.playlistmaker_ver2.base.ActivityBase

class ActivityMediateka : ActivityBase() {
    private lateinit var binding: ActivityMediatekaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediatekaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupStatusBar(androidx.appcompat.R.attr.colorPrimary)

        binding.bBackToMain.setOnClickListener(DebounceClickListener {
            finish()
        })
    }
}