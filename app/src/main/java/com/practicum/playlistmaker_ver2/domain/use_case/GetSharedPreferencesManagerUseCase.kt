package com.practicum.playlistmaker_ver2.domain.use_case

import android.content.Context
import com.practicum.playlistmaker_ver2.util.SharedPreferencesManager

class GetSharedPreferencesManagerUseCase {
    fun execute(context: Context): SharedPreferencesManager {
        return SharedPreferencesManager(context)
    }
}