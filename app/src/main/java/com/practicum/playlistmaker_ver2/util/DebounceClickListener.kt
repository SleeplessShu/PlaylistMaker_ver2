package com.practicum.playlistmaker_ver2.util

import android.os.Handler
import android.os.Looper
import android.view.View

class DebounceClickListener(
    private val interval: Long = 500L,
    private val onDebounceClick: (View) -> Unit
) : View.OnClickListener {
    private var canClick = true
    private val handler = Handler(Looper.getMainLooper())
    override fun onClick(v: View) {
        if (canClick) {
            canClick = false
            onDebounceClick(v)
            handler.postDelayed({ canClick = true }, interval)
        }
    }

}