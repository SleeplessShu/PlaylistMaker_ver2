package com.practicum.playlistmaker_ver2.search.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker_ver2.creator.Creator
import com.practicum.playlistmaker_ver2.databinding.ActivitySearchBinding
import com.practicum.playlistmaker_ver2.search.domain.models.Track
import com.practicum.playlistmaker_ver2.base.ActivityBase
import com.practicum.playlistmaker_ver2.search.ui.adapters.TrackAdapter
import com.practicum.playlistmaker_ver2.player.ui.ActivityPlayer
import com.practicum.playlistmaker_ver2.util.DebounceClickListener
import java.util.concurrent.Executors

class ActivitySearch : ActivityBase() {

    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModels {
        Creator.provideSearchViewModelFactory(
            Creator.provideSearchInteractor(
                Creator.provideTracksInteractor(
                    connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager,
                    executor = Executors.newCachedThreadPool()
                ),
                Creator.provideClickedTracksInteractor(
                    sharedPreferences = getSharedPreferences(
                        "previous_search_result",
                        Context.MODE_PRIVATE
                    ),
                    gson = Gson()
                )
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupObservers()
        restoreSearchState(savedInstanceState)
    }

    private fun setupUI() {
        binding.trackList.layoutManager = LinearLayoutManager(this)
        binding.trackList.adapter = TrackAdapter(
            emptyList(),
            onRetry = { viewModel.searchTracks() },
            onItemClick = { track ->
                viewModel.addToSearchHistory(track)
                startPlayer(this, track)
            })

        binding.bEraseText.setOnClickListener {
            binding.queryInput.text.clear()
            hideKeyboard()
        }

        binding.bEraseHistory.setOnClickListener {
            viewModel.clearSearchHistory()
            hideKeyboard()
        }

        binding.bBackToMain.setOnClickListener(DebounceClickListener { finish() })

        binding.queryInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onSearchQueryChanged(s.toString())
                viewModel.searchDebounce()
                binding.bEraseText.isVisible = s?.isNotEmpty() == true
            }

            override fun afterTextChanged(s: Editable?) {


            }
        })

        binding.queryInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && binding.queryInput.text.isNotEmpty()) {
                hideKeyboard()
                viewModel.searchTracks()
                true
            } else {
                false
            }
        }
    }

    private fun setupObservers() {
        viewModel.tracks.observe(this) { tracks ->
            val adapter = binding.trackList.adapter as? TrackAdapter
            adapter?.updateTracks(tracks, viewModel.viewType.value ?: TrackAdapter.VIEW_TYPE_EMPTY)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.isVisible = isLoading
            binding.trackList.isVisible = !isLoading
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            (binding.trackList.adapter as? TrackAdapter)?.updateTracks(
                emptyList(),
                TrackAdapter.VIEW_TYPE_NO_INTERNET
            )
        }

        viewModel.isHistoryVisible.observe(this) { isVisible ->
            binding.bEraseHistory.isVisible = isVisible
        }
    }

    private fun restoreSearchState(savedInstanceState: Bundle?) {
        savedInstanceState?.getString(SEARCH_TEXT_KEY)?.let { viewModel.restoreSearchState(it) }
        binding.queryInput.setText(viewModel.currentQuery)
    }

    private fun startPlayer(context: Context, track: Track) {
        val intent = Intent(context, ActivityPlayer::class.java).apply {
            putExtra("trackData", track)
        }
        startActivity(intent)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    companion object {
        private const val SEARCH_TEXT_KEY = "SEARCH_TEXT"
    }
}
