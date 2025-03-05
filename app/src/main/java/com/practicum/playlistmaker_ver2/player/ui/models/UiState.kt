package com.practicum.playlistmaker_ver2.player.ui.models

import com.google.android.material.bottomsheet.BottomSheetBehavior

data class UiState(
    val isLiked: Boolean = false,
    val inPlaylist: Boolean = false,
    val bottomSheet: Int = BottomSheetBehavior.STATE_HIDDEN,
    val overlayVisibility: Boolean = false,
    val messageState: MessageState = MessageState.NOTHING,
    val playlistName: String = ""
)
