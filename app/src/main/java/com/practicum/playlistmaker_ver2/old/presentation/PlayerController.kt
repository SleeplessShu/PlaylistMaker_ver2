import android.media.MediaPlayer
import android.os.Handler
import com.practicum.playlistmaker_ver2.old.domain.models.PlayerTrack
import com.practicum.playlistmaker_ver2.old.domain.models.Track
import com.practicum.playlistmaker_ver2.old.domain.repository.PlayerControllerInterface
import com.practicum.playlistmaker_ver2.old.presentation.mapper.TrackToPlayerTrackMapper

class PlayerController(
    private val mainThreadHandler: Handler,
    private val playerListener: PlayerListener,
    private val trackToPlayerTrackMapper: TrackToPlayerTrackMapper
) : PlayerControllerInterface {

    companion object {
        private const val DELAY = 200L
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val STATE_STOPPED = 4
    }

    private var playingTimeCounter: Runnable? = null
    private var playerState = STATE_DEFAULT
    private lateinit var mediaPlayer: MediaPlayer
    private var trackTime: String = ""
    private lateinit var previewUrl: String
    private lateinit var playerTrack: PlayerTrack

    override fun onCreate(currentTrack: Track) {
        playerTrack = trackToPlayerTrackMapper.map(currentTrack)
        trackTime = playerTrack.trackTime
        previewUrl = playerTrack.previewUrl
        initializePlayer()
        playerListener.onTrackLoaded(playerTrack)
    }

    override fun onPause() {
        pausePlayer()
    }

    override fun onStop() {
        stopPlayer()
    }

    override fun onDestroy() {
        mediaPlayer.release()
        playingTimeCounter?.let {
            mainThreadHandler.removeCallbacks(it)
        }
    }

    override fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> pausePlayer()
            STATE_PREPARED, STATE_STOPPED, STATE_PAUSED -> startPlayer()
        }
    }

    private fun initializePlayer() {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(previewUrl)
            prepareAsync()
            setOnPreparedListener {
                playerState = STATE_PREPARED
                playerListener.onPlayerPrepared()
            }
            setOnCompletionListener {
                playerState = STATE_STOPPED
                playerListener.onPlayerStopped()
                playingTimeCounter?.let { mainThreadHandler.removeCallbacks(it) }
            }
            setOnErrorListener { _, what, extra ->
                playerListener.onPlayerError("Error occurred: $what, $extra")
                true
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        startPlayingTimeCounter()
        playerListener.onPlayerStarted()
    }

    private fun pausePlayer() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            playerState = STATE_PAUSED
            playingTimeCounter?.let { mainThreadHandler.removeCallbacks(it) }
            playerListener.onPlayerPaused()
        }
    }

    private fun stopPlayer() {
        mediaPlayer.stop()
        playerState = STATE_STOPPED
        playingTimeCounter?.let { mainThreadHandler.removeCallbacks(it) }
        playerListener.onPlayerStopped()
    }

    private fun startPlayingTimeCounter() {
        playingTimeCounter = createPlayingTimeCounterTask()
        mainThreadHandler.post(playingTimeCounter!!)
    }

    private fun createPlayingTimeCounterTask(): Runnable {
        return object : Runnable {
            override fun run() {
                val currentTime = mediaPlayer.currentPosition.toLong()
                playerListener.onPlayTimeUpdate(currentTime)
                mainThreadHandler.postDelayed(this, DELAY)
            }
        }
    }

    interface PlayerListener {
        fun onTrackLoaded(track: PlayerTrack)
        fun onPlayerPrepared()
        fun onPlayerStarted()
        fun onPlayerPaused()
        fun onPlayerStopped()
        fun onPlayTimeUpdate(currentTime: Long)
        fun onPlayerError(errorMessage: String)
    }
}