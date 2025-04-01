package com.practicum.playlistmaker_ver2.playlist.presentation

import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker_ver2.player.ui.models.MessageState
import com.practicum.playlistmaker_ver2.playlist_editor.domain.models.PlaylistEntityPresentation

data class PlaylistPresentationState (
    val playlistEntity: PlaylistEntityPresentation,
    val overlayVisibility: Boolean = false,
    val bottomSheetTracks: Int = BottomSheetBehavior.STATE_HALF_EXPANDED,
    val bottomSheetOptions: Int = BottomSheetBehavior.STATE_HIDDEN,
    val messageState: MessageState
)