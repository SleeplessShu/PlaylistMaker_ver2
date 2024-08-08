package com.practicum.playlistmaker_ver2.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker_ver2.util.DebounceClickListener
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.util.Creator
import com.practicum.playlistmaker_ver2.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker_ver2.domain.use_case.GetNightModeStatusUseCase
import com.practicum.playlistmaker_ver2.domain.use_case.SetNightModeStatusUseCase
import com.practicum.playlistmaker_ver2.ui.base.ActivityBase
import com.practicum.playlistmaker_ver2.util.Intent.AgreementIntentHelper
import com.practicum.playlistmaker_ver2.util.Intent.EmailIntentHelper
import com.practicum.playlistmaker_ver2.util.Intent.ShareIntentHelper

class ActivitySettings : ActivityBase() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var setNightModeStatusUseCase: SetNightModeStatusUseCase
    private lateinit var getNightModeStatusUseCase: GetNightModeStatusUseCase

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNightModeStatusUseCase = Creator.provideSetNightModeStatusUseCase(this)
        getNightModeStatusUseCase = Creator.provideGetNightModeStatusUseCase(this)
        val isNightModeOn = getNightModeStatusUseCase.execute()

        setupStatusBar(androidx.appcompat.R.attr.colorPrimary)

        binding.switcherTheme.isChecked = isNightModeOn
        binding.switcherTheme.setOnCheckedChangeListener { _, isChecked ->
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
            setNightModeStatusUseCase.execute(isChecked)
        }

        binding.bBackToMain.setOnClickListener(DebounceClickListener {
            finish()
        })

        binding.bMailToSupport.setOnClickListener(DebounceClickListener {
            sendSupportEmail()
        })

        binding.bShareApp.setOnClickListener(DebounceClickListener {
            shareApp()
        })

        binding.bOpenAgreementWeb.setOnClickListener(DebounceClickListener {
            openAgreementWeb()
        })
    }

    private fun sendSupportEmail() {
        val mailToSupportIntent = EmailIntentHelper.createSupportEmailIntent(
            getString(R.string.supportEmail),
            getString(R.string.mailToSupportSubject),
            getString(R.string.mailToSupportText)
        )
        startActivity(mailToSupportIntent)
    }

    private fun shareApp() {
        val shareIntent = ShareIntentHelper.createShareAppIntent(
            context = this,
            shareText = getString(R.string.linkToAndroidCourse)
        )
        startActivity(shareIntent)
    }

    private fun openAgreementWeb() {
        val agreementWebIntent = AgreementIntentHelper.createAgreementWebIntent(
            context = this,
            link = getString(R.string.linkToAgreement)
        )
        startActivity(agreementWebIntent)
    }
}