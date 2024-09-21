package com.practicum.playlistmaker_ver2.settings.ui


import android.os.Bundle
import com.practicum.playlistmaker_ver2.util.DebounceClickListener
import com.practicum.playlistmaker_ver2.creator.Creator
import com.practicum.playlistmaker_ver2.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker_ver2.base.ActivityBase
import androidx.activity.viewModels

class ActivitySettings : ActivityBase() {


    private val viewModelSettings by viewModels<SettingsViewModel> {
        SettingsViewModel.getViewModelFactory(
            Creator.provideSharingInteractor(),
            Creator.provideSettingsInteractor()
        )
    }
    private lateinit var binding: ActivitySettingsBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupStatusBar(androidx.appcompat.R.attr.colorPrimary)

        binding.switcherTheme.isChecked = viewModelSettings.getTheme()
        binding.switcherTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModelSettings.setTheme(isChecked)
        }

        binding.bBackToMain.setOnClickListener(DebounceClickListener {
            finish()
        })

        binding.bMailToSupport.setOnClickListener(DebounceClickListener {
            viewModelSettings.supportSend()
        })

        binding.bShareApp.setOnClickListener(DebounceClickListener {
            viewModelSettings.shareApp()
        })

        binding.bOpenAgreementWeb.setOnClickListener(DebounceClickListener {
            viewModelSettings.openTerm()
        })
    }
}