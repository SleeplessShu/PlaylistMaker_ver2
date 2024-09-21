package com.practicum.playlistmaker_ver2.settings.ui


import android.os.Bundle
import com.practicum.playlistmaker_ver2.util.DebounceClickListener

import com.practicum.playlistmaker_ver2.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker_ver2.base.ActivityBase
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivitySettings : ActivityBase() {


    private val viewModel: SettingsViewModel by viewModel()
    private lateinit var binding: ActivitySettingsBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupStatusBar(androidx.appcompat.R.attr.colorPrimary)

        binding.switcherTheme.isChecked = viewModel.getTheme()
        binding.switcherTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setTheme(isChecked)
        }

        binding.bBackToMain.setOnClickListener(DebounceClickListener {
            finish()
        })

        binding.bMailToSupport.setOnClickListener(DebounceClickListener {
            viewModel.supportSend()
        })

        binding.bShareApp.setOnClickListener(DebounceClickListener {
            viewModel.shareApp()
        })

        binding.bOpenAgreementWeb.setOnClickListener(DebounceClickListener {
            viewModel.openTerm()
        })
    }
}