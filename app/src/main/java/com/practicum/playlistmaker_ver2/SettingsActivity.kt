package com.practicum.playlistmaker_ver2

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : BaseActivity() {
    companion object {
        const val NIGHT_MODE_KEY = "NightMode"
        const val SETTINGS_KEY = "Settings"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val switchTheme = findViewById<FrameLayout>(R.id.switchTheme)
        val switcherTheme = findViewById<SwitchMaterial>(R.id.switcherTheme)
        val prefs = getSharedPreferences(SETTINGS_KEY, MODE_PRIVATE)
        val isNightModeOn = prefs.getBoolean(
            NIGHT_MODE_KEY,
            AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        )

        setupStatusBar(androidx.appcompat.R.attr.colorPrimary)

        switcherTheme.isChecked = isNightModeOn
        switcherTheme.setOnCheckedChangeListener { _, isChecked ->
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
            prefs.edit().putBoolean(NIGHT_MODE_KEY, isChecked).apply()
        }


        switchTheme.setOnClickListener {
            switcherTheme.isChecked = !switcherTheme.isChecked
        }

        val onPressBackToMain = findViewById<LinearLayout>(R.id.buttonBackToMain)
        onPressBackToMain.setOnClickListener {
            finish()
        }

        val supportApp = findViewById<FrameLayout>(R.id.mailToSupport)
        supportApp.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.supportEmail)))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mailToSupportSubject))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.mailToSupportText))
            startActivity(supportIntent)
        }

        val shareApp = findViewById<FrameLayout>(R.id.shareApp)
        shareApp.setOnClickListener {

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.linkToAndroidCourse))
            startActivity(shareIntent)
        }

        val toAgreementWeb = findViewById<FrameLayout>(R.id.toAgreementWeb)
        toAgreementWeb.setOnClickListener {
            val agreementIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.linkToAgreement)))
            startActivity(agreementIntent)
        }
    }
}