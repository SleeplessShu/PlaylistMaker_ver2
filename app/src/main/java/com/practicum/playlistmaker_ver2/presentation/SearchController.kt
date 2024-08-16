/*package com.practicum.playlistmaker_ver2.presentation

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker_ver2.databinding.ActivitySearchBinding
import com.practicum.playlistmaker_ver2.domain.models.Track
import com.practicum.playlistmaker_ver2.util.Creator
import com.practicum.playlistmaker_ver2.ui.search.TrackAdapter
import com.practicum.playlistmaker_ver2.util.DebounceClickListener
import java.util.concurrent.Executors

class SearchController(
    private val appContext: Context,
    private val binding: ActivitySearchBinding
) {
    private val connectivityManager =
        appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val executors = Executors.newCachedThreadPool()
    private val trackInteractor = Creator.provideTracksInteractor(connectivityManager, executors)
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var getClickedTracksUseCase: GetClickedTracksUseCase
    private lateinit var eraseClickedTracksUseCase: EraseClickedTracksUseCase
    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener

    private companion object {
        const val SEARCH_TEXT_KEY = "SEARCH_TEXT"
        const val SEARCH_DEBOUNCE_DELAY = 1000L
        const val CLICKED_TRACKS_REPOSITORY_NAME: String = "previous_search_result"
    }

    private val tracks = ArrayList<Track>()
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchRequest() }
    private var isSearchRequestedManually = false


    //ВОТ ТУТ НАДО ПОДУМАТЬ, ЗАЧЕМ МНЕ ЭТО
    private val sharedPreferences: SharedPreferences = appContext.getSharedPreferences(
        CLICKED_TRACKS_REPOSITORY_NAME, Context.MODE_PRIVATE
    )
    private val gson = Gson()
    private var sharedPreferencesManager =
        Creator.provideSharedPreferencesManager(appContext)
    //ВОТ ТУТ НАДО ПОДУМАТЬ, ЗАЧЕМ МНЕ ЭТО


    fun onCreate(savedInstanceState: Bundle?) {
        getClickedTracksUseCase =
            Creator.provideGetClickedTracksUseCase(sharedPreferences, gson = gson)
        eraseClickedTracksUseCase =
            Creator.provideEraseClickedTracksUseCase(sharedPreferences, gson = gson)
        binding.trackList.layoutManager =
            LinearLayoutManager(appContext, LinearLayoutManager.VERTICAL, false)

        trackAdapter =
            TrackAdapter(
                sharedPreferences = sharedPreferences,
                gson = gson,
                emptyList(),
                TrackAdapter.VIEW_TYPE_EMPTY
            ) {
                trackAdapter.updateTracks(emptyList(), TrackAdapter.VIEW_TYPE_EMPTY)
                searchRequest()
            }
        binding.trackList.adapter = trackAdapter

        binding.bBackToMain.setOnClickListener(DebounceClickListener {
            appContext.finish()
        })

        binding.queryInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable) {
                binding.bEraseText.isVisible = s.isNotEmpty()
            }
        })

        binding.queryInput.setOnEditorActionListener { v, actionId, _ ->
            val imeActionDone = actionId == EditorInfo.IME_ACTION_DONE
            if (imeActionDone) {
                if (binding.queryInput.text.isNotEmpty()) {
                    val imm =
                        appContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                    isSearchRequestedManually = true
                    searchRequest()
                }
            }
            imeActionDone
        }

        binding.bEraseText.setOnClickListener {
            binding.queryInput.text.clear()
            val imm = appContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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
            binding.trackList.isVisible = false
            binding.bEraseHistory.isVisible = false
            binding.progressBar.isVisible = true

            trackInteractor.searchTrack(binding.queryInput.text.toString()) { foundTracks, errorMessage ->

                handler.post {
                    binding.progressBar.isVisible = false
                    when {
                        foundTracks != null -> {
                            tracks.clear()
                            tracks.addAll(foundTracks)
                            binding.trackList.isVisible = true
                            trackAdapter.updateTracks(tracks, TrackAdapter.VIEW_TYPE_ITEM)
                        }

                        errorMessage != null -> handleNoInternet()
                        else -> trackAdapter.updateTracks(
                            emptyList(),
                            TrackAdapter.VIEW_TYPE_NOTHING_FOUND
                        )
                    }
                }
            }
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
}*/