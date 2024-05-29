package com.practicum.playlistmaker_ver2

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : BaseActivity() {
    companion object {
        const val SEARCH_TEXT_KEY = "SEARCH_TEXT"
    }

    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService: iTunesApi = retrofit.create(iTunesApi::class.java)
    private lateinit var searchButton: EditText
    private lateinit var clearButton: ImageView
    private lateinit var clearHistoryButton: Button
    private lateinit var trackList: RecyclerView
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener
    private val sharedPreferencesKey: String = "clicked_tracks"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        sharedPreferencesManager = SharedPreferencesManager(this)

        trackList = findViewById(R.id.searchResult)
        trackList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        trackAdapter =
            TrackAdapter(sharedPreferencesManager, emptyList(), TrackAdapter.VIEW_TYPE_EMPTY) {
            trackAdapter.updateTracks(emptyList(), TrackAdapter.VIEW_TYPE_EMPTY)
                searchTracks(searchButton.text.toString())
        }
        trackList.adapter = trackAdapter

        setupStatusBar(androidx.appcompat.R.attr.colorPrimary)

        val onPressBackToMain: LinearLayout = findViewById(R.id.buttonBackToMain)
        onPressBackToMain.setOnClickListener {
            finish()
        }

        searchButton = findViewById(R.id.text_SearchInput)
        clearButton = findViewById(R.id.clear_text)
        clearHistoryButton = findViewById(R.id.bClearHistory)

        savedInstanceState?.getString(SEARCH_TEXT_KEY)?.let {
            searchButton.setText(it)
            if (it.isNotEmpty()) {
                clearButton.visibility = View.VISIBLE
                clearHistoryButton.visibility = View.INVISIBLE
            }
        }

        searchButton.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                clearButton.visibility = if (s.isNotEmpty()) View.VISIBLE else View.GONE
            }
        })

        searchButton.setOnEditorActionListener { v, actionId, _ ->
            //этот тост нужен для тестирования, тк эмулятор осуществляет поиск около 30+ секунд
            Toast.makeText(this, "Поиск запущен", Toast.LENGTH_SHORT).show()
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (searchButton.text.isNotEmpty()) {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                    searchTracks(searchButton.text.toString())
                }
                true
            } else {
                false
            }
        }

        clearButton.setOnClickListener {
            searchButton.text.clear()
            trackAdapter.updateTracks(emptyList(), TrackAdapter.VIEW_TYPE_EMPTY)
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        clearHistoryButton.setOnClickListener {
            sharedPreferencesManager.removeData(sharedPreferencesKey)
            updateTrackList()

        }

        listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (sharedPreferencesKey == key) {
                if (searchButton.text.isEmpty()) {
                    updateTrackList()
                }
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
        outState.putString(SEARCH_TEXT_KEY, searchButton.text.toString())
    }

    private fun searchTracks(query: String) {
        iTunesService.searchTrack(query).enqueue(object : Callback<iTunesResponse> {
            override fun onResponse(
                call: Call<iTunesResponse>,
                response: Response<iTunesResponse>
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

            override fun onFailure(call: Call<iTunesResponse>, t: Throwable) {
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
            clearHistoryButton.visibility = View.VISIBLE
        } else {
            trackAdapter.updateTracks(emptyList(), TrackAdapter.VIEW_TYPE_EMPTY)
            clearHistoryButton.visibility = View.GONE
        }
    }
}
