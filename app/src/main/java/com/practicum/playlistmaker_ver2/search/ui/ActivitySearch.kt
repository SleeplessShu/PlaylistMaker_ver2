package com.practicum.playlistmaker_ver2.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker_ver2.base.ActivityBase
import com.practicum.playlistmaker_ver2.databinding.ActivitySearchBinding
import com.practicum.playlistmaker_ver2.player.ui.ActivityPlayer
import com.practicum.playlistmaker_ver2.player.ui.mappers.TrackToPlayerTrackMapper
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerTrack
import com.practicum.playlistmaker_ver2.search.domain.models.Track
import com.practicum.playlistmaker_ver2.search.ui.adapters.TrackAdapter
import com.practicum.playlistmaker_ver2.search.ui.models.SearchState
import com.practicum.playlistmaker_ver2.util.DebounceClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel


class ActivitySearch : ActivityBase() {

    private var savedSearchText = AMOUNT_DEF
    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupStatusBar(androidx.appcompat.R.attr.colorPrimary)
        setupUI()
        setupObservers()
        restoreSearchState(savedInstanceState)
    }

    private fun setupUI() {
        binding.trackList.layoutManager = LinearLayoutManager(this)
        binding.trackList.adapter = TrackAdapter(
            emptyList(),
            onRetry = { viewModel.searchTracks(viewModel.currentQuery) },
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
                viewModel.searchTracks(viewModel.currentQuery)
                true
            } else {
                false
            }
        }
    }

    private fun setupObservers() {
        viewModel.searchViewState.observe(this) { viewState ->
            val adapter = binding.trackList.adapter as? TrackAdapter

            when (viewState.state) {
                SearchState.EMPTY -> {
                    // Пустое состояние: очищаем список и скрываем прогресс
                    adapter?.updateTracks(emptyList(), TrackAdapter.VIEW_TYPE_EMPTY)
                    binding.progressBar.isVisible = false
                    binding.trackList.isVisible = false
                    binding.bEraseHistory.isVisible = false
                }

                SearchState.HISTORY -> {
                    // Показ истории поиска
                    adapter?.updateTracks(viewState.tracks, TrackAdapter.VIEW_TYPE_ITEM)
                    binding.progressBar.isVisible = false
                    binding.trackList.isVisible = true
                    binding.bEraseHistory.isVisible = true
                }

                SearchState.TRACKS -> {
                    // Показ найденных треков
                    adapter?.updateTracks(viewState.tracks, TrackAdapter.VIEW_TYPE_ITEM)
                    binding.progressBar.isVisible = false
                    binding.trackList.isVisible = true
                    binding.bEraseHistory.isVisible = false
                }

                SearchState.SEARCHING -> {
                    // Во время поиска показываем индикатор загрузки
                    adapter?.updateTracks(emptyList(), TrackAdapter.VIEW_TYPE_EMPTY)
                    binding.progressBar.isVisible = true
                    binding.trackList.isVisible = false
                    binding.bEraseHistory.isVisible = false
                }

                SearchState.NOTHING_FOUND -> {
                    // Ничего не найдено: показываем пустой список с сообщением
                    adapter?.updateTracks(emptyList(), TrackAdapter.VIEW_TYPE_NOTHING_FOUND)
                    binding.progressBar.isVisible = false
                    binding.trackList.isVisible = true
                    binding.bEraseHistory.isVisible = false
                }

                SearchState.NO_INTERNET -> {
                    // Ошибка сети: показываем сообщение и пустой список
                    adapter?.updateTracks(emptyList(), TrackAdapter.VIEW_TYPE_NO_INTERNET)
                    binding.progressBar.isVisible = false
                    binding.trackList.isVisible = true
                    binding.bEraseHistory.isVisible = false
                    Toast.makeText(this, viewState.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        savedSearchText = binding.queryInput.text.toString()
        outState.putString(SEARCH_TEXT_KEY, binding.queryInput.text.toString())

    }

    private fun restoreSearchState(savedInstanceState: Bundle?) {
        binding.queryInput.setText("")
        savedInstanceState?.getString(SEARCH_TEXT_KEY)?.let { viewModel.restoreSearchState(it) }
        binding.queryInput.setText(viewModel.currentQuery)


    }

    private fun startPlayer(context: Context, track: Track) {
        val playerTrack: PlayerTrack = TrackToPlayerTrackMapper.map(track)
        val intent = Intent(context, ActivityPlayer::class.java).apply {
            putExtra("trackData", playerTrack)
        }
        startActivity(intent)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    companion object {
        private const val SEARCH_TEXT_KEY = "SEARCH_TEXT"
        const val AMOUNT_DEF = ""
    }
}
