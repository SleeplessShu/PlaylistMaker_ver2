package com.practicum.playlistmaker_ver2.ui.search

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.util.Creator
import com.practicum.playlistmaker_ver2.databinding.ActivitySearchBinding
import com.practicum.playlistmaker_ver2.domain.api.TrackInteractor
import com.practicum.playlistmaker_ver2.domain.interactor.ClickedTracksInteractor
import com.practicum.playlistmaker_ver2.domain.models.Track
import com.practicum.playlistmaker_ver2.ui.base.ActivityBase
import com.practicum.playlistmaker_ver2.ui.player.ActivityPlayer
import com.practicum.playlistmaker_ver2.util.DebounceClickListener
import com.practicum.playlistmaker_ver2.util.SharedPreferencesManager
import java.util.concurrent.Executors


class ActivitySearch : ActivityBase() {
    private companion object {
        const val AMOUNT_DEF = ""
        const val SEARCH_TEXT_KEY = "SEARCH_TEXT"
        const val SEARCH_DEBOUNCE_DELAY = 1000L
        const val CLICKED_TRACKS_REPOSITORY_NAME: String = "previous_search_result"
    }

    private lateinit var binding: ActivitySearchBinding
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener
    private lateinit var clickedTracksInteractor: ClickedTracksInteractor
    private lateinit var trackInteractor: TrackInteractor
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var connectivityManager: ConnectivityManager

    private val executors = Executors.newCachedThreadPool()
    private val tracks = ArrayList<Track>()
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchRequest() }


    private var isSearchRequestedManually = false
    private var savedSearchText = AMOUNT_DEF


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setupStatusBar(androidx.appcompat.R.attr.colorPrimary)
        val appContext = applicationContext

        sharedPreferencesManager = Creator.provideSharedPreferencesManager(appContext)

        connectivityManager =
            appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        trackInteractor = Creator.provideTracksInteractor(connectivityManager, executors)

        val sharedPreferences: SharedPreferences = appContext.getSharedPreferences(
            CLICKED_TRACKS_REPOSITORY_NAME, Context.MODE_PRIVATE
        )
        val gson = Gson()
        clickedTracksInteractor = Creator.provideClickedTracksInteractor(
            sharedPreferences = sharedPreferences, gson = gson
        )
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.trackList.layoutManager =
            LinearLayoutManager(appContext, LinearLayoutManager.VERTICAL, false)



        binding.bEraseText.setOnClickListener {
            binding.queryInput.text.clear()
            hideKeyboard()
        }

        binding.bEraseHistory.setOnClickListener {
            clearSearchHistory()
        }

        listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (binding.queryInput.text.isEmpty()) {
                updateTrackList()
            }
        }

        binding.bBackToMain.setOnClickListener(DebounceClickListener {
            this.finish()
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
                    isSearchRequestedManually = true
                    searchRequest()
                }
            }
            imeActionDone
        }

        trackAdapter = TrackAdapter(
            trackData = emptyList(),
            viewType = TrackAdapter.VIEW_TYPE_EMPTY,
            onRetry = { searchRequest() },
            onItemClick = {
                clickedTracksInteractor.addTrack(it)
                startPlayer(appContext = appContext, track = it)
            }
        )
        trackAdapter.updateTracks(emptyList(), TrackAdapter.VIEW_TYPE_EMPTY)

        binding.trackList.adapter = trackAdapter
        sharedPreferencesManager.registerOnSharedPreferenceChangeListener(listener)

        savedInstanceState?.getString(SEARCH_TEXT_KEY)?.let {
            updateButtonVisibility(it)
        }

        savedInstanceState?.let {
            binding.queryInput.setText(it.getString(SEARCH_TEXT_KEY))
        }

        updateTrackList()
    }


    override fun onDestroy() {
        super.onDestroy()
        sharedPreferencesManager.unregisterOnSharedPreferenceChangeListener(listener)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        savedSearchText = binding.queryInput.text.toString()
        outState.putString(SEARCH_TEXT_KEY, savedSearchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedSearchText = savedInstanceState.getString(SEARCH_TEXT_KEY, AMOUNT_DEF)
        binding.queryInput.setText(savedSearchText)
    }

    private fun startPlayer(appContext: Context, track: Track) {
        val playerIntent = Intent(appContext, ActivityPlayer::class.java).apply {
            putExtra("trackData", track)
        }
        clickedTracksInteractor.addTrack(track)
        startActivity(playerIntent)
    }

    private fun searchRequest() {
        hideKeyboard()
        if (binding.queryInput.text.isNotEmpty()) {
            binding.trackList.isVisible = false
            binding.bEraseHistory.isVisible = false
            binding.progressBar.isVisible = true

            trackInteractor.doRequest(binding.queryInput.text.toString()) { foundTracks, errorMessage ->
                Log.d("DEBUG_SHU", "tracks $foundTracks, errorMessage $errorMessage")
                handler.post {
                    binding.progressBar.isVisible = false
                    binding.trackList.isVisible = true
                    if (foundTracks != null) {
                        when {
                            foundTracks.isNotEmpty() -> {
                                Log.d("DEBUG_SHU", "$foundTracks.isNotEmpty()")
                                tracks.clear()
                                tracks.addAll(foundTracks)
                                trackAdapter.updateTracks(tracks, TrackAdapter.VIEW_TYPE_ITEM)

                            }

                            foundTracks.isEmpty() -> {
                                Log.d("DEBUG_SHU", "$foundTracks.isEmpty()")
                                handleNothingFound()
                            }

                        }
                    } else {
                        Toast.makeText(this, "Error $errorMessage", Toast.LENGTH_SHORT).show()
                        handleNoInternet()
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

    private fun handleNothingFound() {
        trackAdapter.updateTracks(emptyList(), TrackAdapter.VIEW_TYPE_NOTHING_FOUND)

    }


    private fun updateTrackList() {
        val previousSearchList = clickedTracksInteractor.getTracks()
        if (previousSearchList.isNotEmpty()) {
            trackAdapter.updateTracks(previousSearchList, TrackAdapter.VIEW_TYPE_ITEM)
            binding.bEraseHistory.isVisible = true
        } else {
            trackAdapter.updateTracks(emptyList(), TrackAdapter.VIEW_TYPE_EMPTY)
            binding.bEraseHistory.isVisible = false
        }
    }

    private fun updateButtonVisibility(searchText: String) {
        binding.queryInput.setText(searchText)
        binding.bEraseText.isVisible = searchText.isNotEmpty()
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private fun searchDebounce() {
        if (!isSearchRequestedManually) {
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }
    }

    private fun clearSearchHistory() {
        clickedTracksInteractor.eraseTracks()
        binding.bEraseHistory.isVisible = false
    }
}
