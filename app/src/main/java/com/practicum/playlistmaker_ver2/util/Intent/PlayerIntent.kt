package com.practicum.playlistmaker_ver2.util.Intent

import android.content.Context
import android.content.Intent
import com.practicum.playlistmaker_ver2.domain.models.Track
import com.practicum.playlistmaker_ver2.ui.player.ActivityPlayer

object PlayerIntent {
    fun startPlayer(context: Context, track: Track): Intent {
        return Intent(context, ActivityPlayer::class.java).apply {
            putExtra("trackData", track)
        }
    }
}