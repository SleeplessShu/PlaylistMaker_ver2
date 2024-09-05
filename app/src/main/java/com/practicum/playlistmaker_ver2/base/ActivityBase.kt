package com.practicum.playlistmaker_ver2.base

import android.content.res.Configuration
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import androidx.annotation.AttrRes
import androidx.appcompat.app.AppCompatActivity

open class ActivityBase : AppCompatActivity() {

    fun setupStatusBar(@AttrRes colorAttribute: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val typedValue = TypedValue()
            theme.resolveAttribute(colorAttribute, typedValue, true)
            val color = typedValue.data

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = color

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val nightModeFlags =
                    resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                val useLightIcons =
                    nightModeFlags == Configuration.UI_MODE_NIGHT_NO // Светлые иконки, если не ночной режим

                window.decorView.systemUiVisibility = if (useLightIcons) {
                    window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                }
            }
        }
    }


}