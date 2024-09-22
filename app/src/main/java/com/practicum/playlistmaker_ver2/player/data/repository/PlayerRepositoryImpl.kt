package com.practicum.playlistmaker_ver2.player.data.repository


import android.media.MediaPlayer
import android.os.Handler


import com.practicum.playlistmaker_ver2.player.domain.repositories.PlayerRepository


class PlayerRepositoryImpl(private var player: MediaPlayer, private val handler: Handler) :
    PlayerRepository {


    private var isPlayerReady: Boolean = false
    private var isPlayerReleased: Boolean = true

    override fun prepare(
        trackUrl: String,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit,
        onError: (what: Int, extra: Int) -> Boolean
    ) {


        try {
            player?.apply {
                setDataSource(trackUrl)
                prepareAsync()

                setOnPreparedListener {
                    handler.post {
                        isPlayerReady = true
                        onPrepared()
                    }
                }

                setOnCompletionListener {
                    handler.post {
                        onCompletion()
                    }
                }

                setOnErrorListener { _, what, extra ->
                    handler.post {
                        onError(what, extra)
                    }
                    true
                }
            }
            isPlayerReleased = false
        } catch (e: Exception) {
            e.printStackTrace()
            handler.post {
                onError(-1, -1)
            }
        }
    }

    override fun startPlayer() {
        if (player != null && isPlayerReady) {
            player?.start()
        }
    }

    override fun pausePlayer() {
        if (player != null && player!!.isPlaying) {
            player?.pause()
        }
    }

    override fun stopPlayer() {
        if (player != null && isPlayerReady) {
            player?.stop()
            isPlayerReady = false
        }
    }

    override fun resetPlayer() {
        if (player != null) {
            player?.reset()
            isPlayerReady = false
        }
    }

    override fun releasePlayer() {
        if (player != null && !isPlayerReleased) {
            player?.release()
            isPlayerReady = false
            isPlayerReleased = true
        }
    }

    override fun isPlaying(): Boolean {
        return player?.isPlaying ?: false
    }

    override fun getCurrentPosition(): Int {
        return player?.currentPosition ?: 0
    }

    override fun seekTo(position: Int) {
        player?.seekTo(position)
    }
}
