package com.practicum.playlistmaker_ver2.playlist.presentation

import android.view.View
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior

class BottomSheetManager (
    private val bottomSheet: View,
    private val overlay: View? = null,
    private val initialState: Int = BottomSheetBehavior.STATE_HIDDEN,
    private val peekHeightRatio: Int = 0,
    private val isHideable: Boolean = true,
) {

    private val behavior: BottomSheetBehavior<View> = BottomSheetBehavior.from(bottomSheet)

    init {
        behavior.peekHeight = peekHeightRatio
        behavior.isHideable = isHideable
        behavior.state = initialState
        /*if (initialState == BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheet.visibility = View.GONE
        }*/
        if (initialState == BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheet.visibility = View.INVISIBLE
        }

        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                /*if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheet.visibility = View.GONE
                    overlay?.isVisible = false
                }*/
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheet.visibility = View.INVISIBLE
                    overlay?.isVisible = false
                }

            }
        })
    }

    fun setState(state: Int) {
        bottomSheet.visibility = View.VISIBLE
        overlay?.isVisible = (state != BottomSheetBehavior.STATE_HIDDEN)
        behavior.state = state

        if (state == BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheet.visibility = View.GONE
        }
    }

    fun expand() {
        setState(BottomSheetBehavior.STATE_EXPANDED)
    }

    fun collapse() {
        setState(BottomSheetBehavior.STATE_COLLAPSED)
    }

    fun halfExpand() {
        setState(BottomSheetBehavior.STATE_HALF_EXPANDED)
    }

    fun hide() {
        setState(BottomSheetBehavior.STATE_HIDDEN)
    }

    fun currentState(): Int = behavior.state
}
