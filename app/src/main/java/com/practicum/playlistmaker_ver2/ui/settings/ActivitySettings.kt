package com.practicum.playlistmaker_ver2.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker_ver2.util.DebounceClickListener
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.util.Creator
import com.practicum.playlistmaker_ver2.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker_ver2.ui.base.ActivityBase


class ActivitySettings : ActivityBase() {
    private companion object {
        const val THEME_SHARED_PREFERENCES_KEY = "NightMode"
    }

    private lateinit var binding: ActivitySettingsBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences = getSharedPreferences(THEME_SHARED_PREFERENCES_KEY, MODE_PRIVATE)
        val themeInteractor = Creator.provideThemeStatusInteractor(sharedPreferences)

        setupStatusBar(androidx.appcompat.R.attr.colorPrimary)

        binding.switcherTheme.isChecked = themeInteractor.getThemeStatus()
        binding.switcherTheme.setOnCheckedChangeListener { _, isChecked ->
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
            themeInteractor.setThemeStatus(isChecked)
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
        val supportIntent = Intent(Intent.ACTION_SENDTO)
        supportIntent.data = Uri.parse("mailto:")
        supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.supportEmail)))
        supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mailToSupportSubject))
        supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.mailToSupportText))
        startActivity(supportIntent)
    }

    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.linkToAndroidCourse))
        startActivity(shareIntent)
    }

    private fun openAgreementWeb() {
        val agreementIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.linkToAgreement)))
        startActivity(agreementIntent)
    }
}