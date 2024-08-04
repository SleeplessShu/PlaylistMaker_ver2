package com.practicum.playlistmaker_ver2.ui.search

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
import com.practicum.playlistmaker_ver2.data.network.ApiResponseITunes
import com.practicum.playlistmaker_ver2.data.network.ApiITunes
import com.practicum.playlistmaker_ver2.util.DebounceClickListener
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.util.SharedPreferencesManager
import com.practicum.playlistmaker_ver2.data.dto.TrackData
import com.practicum.playlistmaker_ver2.databinding.ActivitySearchBinding
import com.practicum.playlistmaker_ver2.ui.base.ActivityBase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ActivitySearch : ActivityBase() {

    companion object {
        const val SEARCH_TEXT_KEY = "SEARCH_TEXT"
        const val sharedPreferencesKey: String = "clicked_tracks"
        const val sharedPreferencesName: String = "previous_search_result"
        const val ITUNES_BASE_URL: String = "https://itunes.apple.com"
        private const val SEARCH_DEBOUNCE_DELAY = 1000L
    }

    private val searchRunnable = Runnable { searchRequest() }
    private val handler = Handler(Looper.getMainLooper())
    private var isSearchRequestedManually = false

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService: ApiITunes = retrofit.create(ApiITunes::class.java)
    private lateinit var binding: ActivitySearchBinding
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferencesManager = SharedPreferencesManager(applicationContext)

        binding.searchResult.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        trackAdapter =
            TrackAdapter(sharedPreferencesManager, emptyList(), TrackAdapter.VIEW_TYPE_EMPTY) {
                trackAdapter.updateTracks(emptyList(), TrackAdapter.VIEW_TYPE_EMPTY)
                searchRequest()
            }
        binding.searchResult.adapter = trackAdapter

        setupStatusBar(androidx.appcompat.R.attr.colorPrimary)

        binding.bBackToMain.setOnClickListener(DebounceClickListener {
            finish()
        })

        savedInstanceState?.getString(SEARCH_TEXT_KEY)?.let {
            updateButtonVisibility(it)
        }

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
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                    isSearchRequestedManually = true
                    searchRequest()
                }
            }
            imeActionDone
        }


        binding.bEraseText.setOnClickListener {
            binding.queryInput.text.clear()
            //trackAdapter.updateTracks(emptyList(), TrackAdapter.VIEW_TYPE_EMPTY)
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        binding.bEraseHistory.setOnClickListener {
            clearSearchHistory()
        }

        listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (sharedPreferencesKey == key && binding.queryInput.text.isEmpty()) {
                updateTrackList()
            }
        }

        sharedPreferencesManager.registerOnSharedPreferenceChangeListener(listener)

        updateTrackList()
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedPreferencesManager.unregisterOnSharedPreferenceChangeListener(listener)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT_KEY, binding.queryInput.text.toString())
    }

    private fun searchRequest() {
        if (binding.queryInput.text.isNotEmpty()) {
            binding.searchResult.visibility = View.GONE
            binding.bEraseHistory.isVisible = false
            binding.progressBar.visibility = View.VISIBLE

            iTunesService.searchTrack(binding.queryInput.text.toString())
                .enqueue(object : Callback<ApiResponseITunes> {
                    override fun onResponse(
                        call: Call<ApiResponseITunes>,
                        response: Response<ApiResponseITunes>
                    ) {
                        binding.progressBar.visibility = View.GONE
                        if (response.isSuccessful) {
                            binding.searchResult.visibility = View.VISIBLE
                            val results: List<TrackData> = response.body()?.results.orEmpty()
                            val viewType = if (results.isEmpty()) {
                                TrackAdapter.VIEW_TYPE_NOTHING_FOUND
                            } else {
                                TrackAdapter.VIEW_TYPE_ITEM
                            }
                            trackAdapter.updateTracks(results, viewType)
                        } else {
                            handleNoInternet()
                        }
                    }

                    override fun onFailure(call: Call<ApiResponseITunes>, t: Throwable) {
                        handleNoInternet()
                    }
                })
        } else {
            updateTrackList()
        }
        isSearchRequestedManually = false
    }


    private fun handleNoInternet() {
        trackAdapter.updateTracks(emptyList(), TrackAdapter.VIEW_TYPE_NO_INTERNET)
    }

    private fun updateTrackList() {
        val previousSearchList = sharedPreferencesManager.getData(sharedPreferencesKey)
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

    private fun searchDebounce() {
        if (!isSearchRequestedManually) {
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }
    }

    private fun clearSearchHistory() {
        sharedPreferencesManager.removeData(sharedPreferencesKey)
        binding.bEraseHistory.isVisible = false
        updateTrackList()
    }
}
