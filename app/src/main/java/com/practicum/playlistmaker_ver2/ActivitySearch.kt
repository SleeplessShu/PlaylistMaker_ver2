package com.practicum.playlistmaker_ver2

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker_ver2.databinding.ActivitySearchBinding
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
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService: Api_iTunes = retrofit.create(Api_iTunes::class.java)
    private lateinit var binding: ActivitySearchBinding
    //private lateinit var searchButton: EditText
    //private lateinit var clearButton: ImageView
    //private lateinit var clearHistoryButton: Button

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
                searchTracks(binding.tiSearch.text.toString())
            }
        binding.searchResult.adapter = trackAdapter

        setupStatusBar(androidx.appcompat.R.attr.colorPrimary)

        binding.bBackToMain.setOnClickListener {
            finish()
        }

        savedInstanceState?.getString(SEARCH_TEXT_KEY)?.let {
            updateButtonVisibility(it)
        }

        binding.tiSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                binding.bEraseText.visibility = if (s.isNotEmpty()) View.VISIBLE else View.GONE
            }
        })

        binding.tiSearch.setOnEditorActionListener { v, actionId, _ ->
            val imeActionDone = actionId == EditorInfo.IME_ACTION_DONE
            if (imeActionDone) {
                if (binding.tiSearch.text.isNotEmpty()) {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                    searchTracks(binding.tiSearch.text.toString())
                }
            }
            imeActionDone
        }


        binding.bEraseText.setOnClickListener {
            binding.tiSearch.text.clear()
            trackAdapter.updateTracks(emptyList(), TrackAdapter.VIEW_TYPE_EMPTY)
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        binding.bEraseHistory.setOnClickListener {
            sharedPreferencesManager.removeData(sharedPreferencesKey)
            updateTrackList()

        }

        listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (sharedPreferencesKey == key && binding.tiSearch.text.isEmpty()) {

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
        outState.putString(SEARCH_TEXT_KEY, binding.tiSearch.text.toString())
    }

    private fun searchTracks(query: String) {
        iTunesService.searchTrack(query).enqueue(object : Callback<Api_Response_iTunes> {
            override fun onResponse(
                call: Call<Api_Response_iTunes>,
                response: Response<Api_Response_iTunes>
            ) {
                if (response.isSuccessful) {
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

            override fun onFailure(call: Call<Api_Response_iTunes>, t: Throwable) {
                handleNoInternet()
            }
        })
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
        binding.tiSearch.setText(searchText)
        binding.bEraseText.isVisible = searchText.isNotEmpty()
        binding.bEraseHistory.isVisible = searchText.isEmpty()
    }
}
