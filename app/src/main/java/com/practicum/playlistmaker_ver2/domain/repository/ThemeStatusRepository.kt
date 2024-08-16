package com.practicum.playlistmaker_ver2.domain.repository

interface ThemeStatusRepository {
    fun setStatus(currentStatus: Boolean)
    fun getStatus(): Boolean
}