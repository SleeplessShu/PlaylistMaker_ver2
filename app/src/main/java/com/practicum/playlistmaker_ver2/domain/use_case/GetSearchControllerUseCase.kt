package com.practicum.playlistmaker_ver2.domain.use_case

import android.app.Activity
import com.practicum.playlistmaker_ver2.databinding.ActivitySearchBinding
import com.practicum.playlistmaker_ver2.presentation.SearchController


class GetSearchControllerUseCase {
    fun execute(
        activity: Activity,
        binding: ActivitySearchBinding
    ): SearchController {
        return SearchController(activity, binding)
    }
}