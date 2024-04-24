package com.practicum.playlistmaker_ver2

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : AppCompatActivity() {
    companion object {
        const val NIGHT_MODE_KEY = "NightMode"
        const val SETTINGS_KEY = "Settings"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val switchTheme = findViewById<SwitchCompat>(R.id.switchTheme)
        val prefs = getSharedPreferences(SETTINGS_KEY, MODE_PRIVATE)
        val isNightModeOn = prefs.getBoolean(
            NIGHT_MODE_KEY,
            AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        )
        switchTheme.isChecked = isNightModeOn

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
            prefs.edit().putBoolean(NIGHT_MODE_KEY, isChecked).apply()
        }

        val onPressBackToMain = findViewById<ImageView>(R.id.buttonBackToMain)
        onPressBackToMain.setOnClickListener {
            finish()
        }

        val supportApp = findViewById<ImageView>(R.id.mailToSupport)
        supportApp.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.supportEmail)))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mailToSupportSubject))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.mailToSupportText))
            startActivity(supportIntent)
        }

        val shareApp = findViewById<ImageView>(R.id.shareApp)
        shareApp.setOnClickListener {

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.linkToAndroidCourse))
            startActivity(shareIntent)
        }

        val toAgreementWeb = findViewById<ImageView>(R.id.toAgreementWeb)
        toAgreementWeb.setOnClickListener {
            val agreementIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.linkToAgreement)))
            startActivity(agreementIntent)
        }
    }
}