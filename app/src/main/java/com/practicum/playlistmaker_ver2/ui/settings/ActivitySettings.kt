package com.practicum.playlistmaker_ver2.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker_ver2.util.DebounceClickListener
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker_ver2.ui.base.ActivityBase

class ActivitySettings : ActivityBase() {
    private lateinit var binding: ActivitySettingsBinding

    companion object {
        const val NIGHT_MODE_KEY = "NightMode"
        const val SETTINGS_KEY = "Settings"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences(SETTINGS_KEY, MODE_PRIVATE)
        val isNightModeOn = prefs.getBoolean(
            NIGHT_MODE_KEY,
            AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        )

        setupStatusBar(androidx.appcompat.R.attr.colorPrimary)

        binding.switcherTheme.isChecked = isNightModeOn
        binding.switcherTheme.setOnCheckedChangeListener { _, isChecked ->
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
            prefs.edit().putBoolean(NIGHT_MODE_KEY, isChecked).apply()
        }


        binding.bSwitchTheme.setOnClickListener {
            binding.switcherTheme.isChecked = !binding.switcherTheme.isChecked
        }

        binding.bBackToMain.setOnClickListener(DebounceClickListener {
            finish()
        })

        binding.bMailToSupport.setOnClickListener(DebounceClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.supportEmail)))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mailToSupportSubject))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.mailToSupportText))
            startActivity(supportIntent)
        })

        binding.bShareApp.setOnClickListener(DebounceClickListener {

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.linkToAndroidCourse))
            startActivity(shareIntent)
        })

        binding.bOpenAgreementWeb.setOnClickListener(DebounceClickListener {
            val agreementIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.linkToAgreement)))
            startActivity(agreementIntent)
        })
    }
}