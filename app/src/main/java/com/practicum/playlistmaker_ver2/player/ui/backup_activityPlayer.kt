/*
package com.practicum.playlistmaker_ver2.player.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.NavHostFragment
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.player.ui.models.PlayerTrack


class ActivityPlayer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val track: PlayerTrack? = intent.getParcelableExtra("trackData")
        Log.d("DEBUG", "Получено в Activity: $track")

        if (track == null) {
            Toast.makeText(this, "Ошибка загрузки трека", Toast.LENGTH_LONG).show()
            finish()
            return
        }
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.player_nav_host_fragment) as? NavHostFragment
        val navController = navHostFragment?.navController

        val bundle = bundleOf("track" to track)
        Log.d("DEBUG", "Передача в Bundle: $bundle")

        navController?.navigate(R.id.playerFragment, bundle)
    }


    private companion object {
        const val GET_TRACK_DATA_FROM_SEARCH = "trackData"
    }
}

*/

/*
    private lateinit var binding: ActivityPlayerBinding

    private val viewModel: PlayerViewModel by viewModel()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val currentTrack: PlayerTrack by lazy {
        requireNotNull(
            intent.serializable(
                GET_TRACK_DATA_FROM_SEARCH
            )
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.setupPlayer(currentTrack.previewUrl)


        val bottomSheetContainer = binding.addPlaylistBottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        setupUI()
        setupObservers()
        initializeViews()
        setupBottomSheetPeekHeight()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releasePlayer()
    }

    private fun initializeViews() {
        binding.apply {
            tvTrackName.text = currentTrack.trackName
            tvArtistName.text = currentTrack.artistName
            tvPrimaryGenreName.text = currentTrack.primaryGenreName
            tvCollectionName.text = currentTrack.collectionName
            tvCountry.text = currentTrack.country
            tvReleaseDate.text = currentTrack.releaseDate
            tvTrackDuration.text = currentTrack.trackTime
            binding.tvPlayTime.text = getString(R.string.defaultTime)
            lifecycleScope.launch {
                viewModel.checkInLiked(currentTrack)
            }
        }

        val radiusPx = formatDpToPx(8)
        Glide.with(this).load(currentTrack.artworkUrl500).placeholder(R.drawable.placeholder)
            .fitCenter().transform(RoundedCorners(radiusPx)).into(binding.ivCollectionImage)
    }

    private fun setupUI() {
        binding.apply {
            binding.bBack.setOnClickListener { onBackPressed() }
            binding.bLike.setOnClickListener { viewModel.addToLikeList(currentTrack) }
            binding.bAddToPlaylist.setOnClickListener { viewModel.addToPlaylist(currentTrack.trackId) }
            binding.bPlay.setOnClickListener { viewModel.playPause() }
            binding.bNewPlaylist.setOnClickListener {
                findNavController(R.id.player_nav_host_fragment)
                    .navigate(R.id.action_playerFragment_to_playlistCreationFragment)
            }
        }
    }

    private fun setupObservers() {
        viewModel.viewState.observe(this) { updatePlayer(it) }
        viewModel.uiState.observe(this) { updateUI(it) }
        viewModel.playlists.observe(this) { playlists ->
            setupRecyclerView(playlists)
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {

                    BottomSheetBehavior.STATE_HIDDEN -> {

                        viewModel.bottomSheetCollapsed()
                    }

                    else -> {

                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

                binding.overlay.animate()
                    .alpha(slideOffset)
                    .setDuration(300)
                    .start()

            }
        })
    }

    private fun updatePlayer(viewState: PlayerViewState) {
        binding.tvPlayTime.text = viewState.currentTime
        when (viewState.playerState) {
            PlayerState.DEFAULT -> {
                binding.bPlay.isEnabled = false
            }

            PlayerState.PREPARED, PlayerState.PAUSED, PlayerState.STOPPED -> {
                binding.bPlay.setImageResource(R.drawable.ic_play)
                binding.bPlay.isEnabled = true
            }

            PlayerState.PLAYING -> {
                binding.bPlay.setImageResource(R.drawable.ic_pause)
            }

            PlayerState.ERROR -> {
                binding.bPlay.setImageResource(R.drawable.ic_play)
                Toast.makeText(this, viewState.errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateUI(uiState: UiState) {
        binding.bAddToPlaylist.setImageResource(
            if (uiState.inPlaylist) R.drawable.ic_add_to_playlist_pushed else R.drawable.ic_add_to_playlist
        )
        binding.bLike.setImageResource(
            if (uiState.isLiked) R.drawable.ic_like_track_pushed else R.drawable.ic_like_track
        )
        binding.overlay.isVisible = uiState.overlayVisibility
        if (uiState.bottomSheet in listOf(
                BottomSheetBehavior.STATE_EXPANDED,
                BottomSheetBehavior.STATE_COLLAPSED,
                BottomSheetBehavior.STATE_HIDDEN,
                BottomSheetBehavior.STATE_HALF_EXPANDED
            )
        ) {
            BottomSheetBehavior.from(binding.addPlaylistBottomSheet).state = uiState.bottomSheet
        }
        when (uiState.messageState) {
            MessageState.NOTHING -> {}
            MessageState.FAIL -> Toast.makeText(
                this,
                getString(R.string.addInPlaylistFail),
                Toast.LENGTH_SHORT
            ).show()

            MessageState.SUCCESS -> Toast.makeText(
                this,
                getString(R.string.addInPlaylistSuccess),
                Toast.LENGTH_SHORT
            ).show()

            MessageState.ALREADY_ADDED

            -> Toast.makeText(
                this,
                getString(R.string.addInPlaylistAlreadyAdded),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setupRecyclerView(
        playlists: List<PlaylistEntityPresentation>
    ) {
        binding.rvPlaylistsList.layoutManager = LinearLayoutManager(this)
        binding.rvPlaylistsList.adapter = TabPlaylistAdapter(
            playlists = playlists,
            layoutType = LayoutType.ForPlayer,
            onItemClick = { playlist ->
                onPlaylistClick(playlist)
            }
        )
    }

    private fun setupBottomSheetPeekHeight() {
        val displayMetrics = resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels
        val peekHeightPercentage = 0.63
        val calculatedPeekHeight = (screenHeight * peekHeightPercentage).toInt()

        bottomSheetBehavior.maxHeight = calculatedPeekHeight
    }

    private fun onPlaylistClick(playlist: PlaylistEntityPresentation) {
        viewModel.onPlaylistClick(playlist, currentTrack)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }



*/
