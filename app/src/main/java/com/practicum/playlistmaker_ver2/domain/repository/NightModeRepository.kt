package com.practicum.playlistmaker_ver2.domain.repository

interface NightModeRepository {
    fun setNightModeStatus(currentStatus: Boolean)
    fun getNightModeStatus(): Boolean
}