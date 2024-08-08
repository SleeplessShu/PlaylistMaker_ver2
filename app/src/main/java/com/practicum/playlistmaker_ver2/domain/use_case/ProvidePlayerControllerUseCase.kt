package com.practicum.playlistmaker_ver2.domain.use_case


import android.app.Activity
import android.os.Handler
import com.practicum.playlistmaker_ver2.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker_ver2.presentation.PlayerController

class ProvidePlayerControllerUseCase {
    fun execute(
        activity: Activity,
        binding: ActivityPlayerBinding,
        mainThreadHandler: Handler
    ): PlayerController {
        return PlayerController(activity, binding, mainThreadHandler)
    }
}