package com.practicum.playlistmaker_ver2.presentation

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker_ver2.databinding.ActivitySearchBinding
import com.practicum.playlistmaker_ver2.domain.api.TracksInteractor
import com.practicum.playlistmaker_ver2.domain.models.Track
import com.practicum.playlistmaker_ver2.domain.use_case.EraseClickedTracksUseCase
import com.practicum.playlistmaker_ver2.domain.use_case.GetClickedTracksUseCase
import com.practicum.playlistmaker_ver2.util.Creator
import com.practicum.playlistmaker_ver2.ui.search.TrackAdapter
import com.practicum.playlistmaker_ver2.util.DebounceClickListener
import com.practicum.playlistmaker_ver2.util.SharedPreferencesManager

class SearchController(
    private val activity: Activity,
    private val binding: ActivitySearchBinding
) {
    private val trackInteractor = Creator.provideTracksInteractor(context = activity)
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var getClickedTracksUseCase: GetClickedTracksUseCase
    private lateinit var eraseClickedTracksUseCase: EraseClickedTracksUseCase
    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener

    companion object {
        const val SEARCH_TEXT_KEY = "SEARCH_TEXT"
        private const val SEARCH_DEBOUNCE_DELAY = 1000L
    }

    private val tracks = ArrayList<Track>()
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchRequest() }
    private var isSearchRequestedManually = false
    private var sharedPreferencesManager = Creator.provideSharedPreferencesManager(activity)

    fun onCreate(savedInstanceState: Bundle?) {
        getClickedTracksUseCase = Creator.provideGetClickedTracksUseCase(activity)
        eraseClickedTracksUseCase = Creator.provideEraseClickedTracksUseCase(activity)
        binding.trackList.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        trackAdapter =
            TrackAdapter(
                context = activity,
                emptyList(),
                TrackAdapter.VIEW_TYPE_EMPTY
            ) {
                trackAdapter.updateTracks(emptyList(), TrackAdapter.VIEW_TYPE_EMPTY)
                searchRequest()
            }
        binding.trackList.adapter = trackAdapter

        binding.bBackToMain.setOnClickListener(DebounceClickListener {
            activity.finish()
        })

        binding.queryInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable) {
                binding.bEraseText.visibility = if (s.isNotEmpty()) View.VISIBLE else View.GONE
            }
        })

        binding.queryInput.setOnEditorActionListener { v, actionId, _ ->
            val imeActionDone = actionId == EditorInfo.IME_ACTION_DONE
            if (imeActionDone) {
                if (binding.queryInput.text.isNotEmpty()) {
                    val imm =
                        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                    isSearchRequestedManually = true
                    searchRequest()
                }
            }
            imeActionDone
        }

        binding.bEraseText.setOnClickListener {
            binding.queryInput.text.clear()
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        binding.bEraseHistory.setOnClickListener {
            clearSearchHistory()
        }

        listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (binding.queryInput.text.isEmpty()) {
                updateTrackList()
            }
        }

        sharedPreferencesManager.registerOnSharedPreferenceChangeListener(listener)

        savedInstanceState?.let {
            binding.queryInput.setText(it.getString(SEARCH_TEXT_KEY))
        }

        updateTrackList()
    }

    fun onDestroy() {
        sharedPreferencesManager.unregisterOnSharedPreferenceChangeListener(listener)
    }

    fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SEARCH_TEXT_KEY, binding.queryInput.text.toString())
    }

    private fun searchRequest() {
        if (binding.queryInput.text.isNotEmpty()) {
            binding.trackList.visibility = View.GONE
            binding.bEraseHistory.isVisible = false
            binding.progressBar.visibility = View.VISIBLE
            trackInteractor.searchTrack(
                binding.queryInput.text.toString(),
                object : TracksInteractor.TrackConsumer {
                    override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                        handler.post {
                            binding.progressBar.visibility = View.GONE
                            if (foundTracks != null) {
                                tracks.clear()
                                tracks.addAll(foundTracks)
                                binding.trackList.visibility = View.VISIBLE
                                trackAdapter.updateTracks(tracks, TrackAdapter.VIEW_TYPE_ITEM)
                            }
                            if (errorMessage != null) {
                                handleNoInternet()
                            } else if (tracks.isEmpty()) {
                                trackAdapter.updateTracks(
                                    emptyList(),
                                    TrackAdapter.VIEW_TYPE_NOTHING_FOUND
                                )
                            }
                        }
                    }
                }
            )
        } else {
            updateTrackList()
        }
        isSearchRequestedManually = false
    }

    private fun handleNoInternet() {
        trackAdapter.updateTracks(emptyList(), TrackAdapter.VIEW_TYPE_NO_INTERNET)
    }

    private fun updateTrackList() {
        val previousSearchList = getClickedTracksUseCase.execute()
        if (previousSearchList.isNotEmpty()) {
            trackAdapter.updateTracks(previousSearchList, TrackAdapter.VIEW_TYPE_ITEM)
            binding.bEraseHistory.isVisible = true
        } else {
            trackAdapter.updateTracks(emptyList(), TrackAdapter.VIEW_TYPE_EMPTY)
            binding.bEraseHistory.isVisible = false
        }
    }

    fun updateButtonVisibility(searchText: String) {
        binding.queryInput.setText(searchText)
        binding.bEraseText.isVisible = searchText.isNotEmpty()
    }

    private fun searchDebounce() {
        if (!isSearchRequestedManually) {
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }
    }

    private fun clearSearchHistory() {
        eraseClickedTracksUseCase.execute()
        binding.bEraseHistory.isVisible = false
        updateTrackList()
    }
}
