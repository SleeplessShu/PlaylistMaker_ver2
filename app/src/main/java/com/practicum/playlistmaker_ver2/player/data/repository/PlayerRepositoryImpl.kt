package com.practicum.playlistmaker_ver2.player.data.repository

import android.media.MediaPlayer
import android.os.Handler
import android.util.Log
import com.practicum.playlistmaker_ver2.player.domain.repositories.PlayerRepository

class PlayerRepositoryImpl(
    private var player: MediaPlayer,
    private val handler: Handler
) : PlayerRepository {

    private var isPrepared = false
    private var isPlayerReleased = true

    override fun prepare(
        trackUrl: String,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit,
        onError: (what: Int, extra: Int) -> Boolean
    ) {
        try {
            resetPlayer()

            player.apply {
                setDataSource(trackUrl)

                setOnPreparedListener {
                    handler.post {
                        isPrepared = true
                        isPlayerReleased = false
                        onPrepared()
                    }
                }

                setOnCompletionListener {
                    handler.post {
                        isPrepared = false
                        onCompletion()
                    }
                }

                setOnErrorListener { _, what, extra ->
                    handler.post {
                        isPrepared = false
                        onError(what, extra)
                    }
                    true
                }

                prepareAsync()
            }

        } catch (e: Exception) {
            Log.e("PlayerRepositoryImpl", "Error preparing player", e)
            handler.post {
                onError(-1, -1)
            }
        }
    }

    override fun startPlayer() {
        if (isPrepared && !isPlayerReleased) {
            try {
                player.start()
            } catch (e: IllegalStateException) {
                Log.e("PlayerRepositoryImpl", "Error starting player", e)
            }
        }
    }

    override fun pausePlayer() {
        if (isPrepared && !isPlayerReleased && player.isPlaying) {
            try {
                player.pause()
            } catch (e: IllegalStateException) {
                Log.e("PlayerRepositoryImpl", "Error pausing player", e)
            }
        }
    }

    override fun stopPlayer() {
        if (isPrepared && !isPlayerReleased) {
            try {
                player.stop()
                isPrepared = false
            } catch (e: IllegalStateException) {
                Log.e("PlayerRepositoryImpl", "Error stopping player", e)
            }
        }
    }

    override fun resetPlayer() {
        if (!isPlayerReleased) {
            try {
                player.reset()
                isPrepared = false
            } catch (e: IllegalStateException) {
                Log.e("PlayerRepositoryImpl", "Error resetting player", e)
            }
        }
    }

    override fun releasePlayer() {
        if (!isPlayerReleased) {
            try {
                player.release()
                isPrepared = false
                isPlayerReleased = true
            } catch (e: IllegalStateException) {
                Log.e("PlayerRepositoryImpl", "Error releasing player", e)
            }
        }
    }

    override fun isPlaying(): Boolean {
        return try {
            if (isPrepared && !isPlayerReleased) {
                player.isPlaying
            } else {
                false
            }
        } catch (e: IllegalStateException) {
            Log.e("PlayerRepositoryImpl", "Error checking isPlaying", e)
            false
        }
    }

    override fun getCurrentPosition(): Int {
        return try {
            if (isPrepared && !isPlayerReleased) {
                player.currentPosition
            } else {
                0
            }
        } catch (e: IllegalStateException) {
            Log.e("PlayerRepositoryImpl", "Error getting current position", e)
            0
        }
    }

    override fun seekTo(position: Int) {
        if (isPrepared && !isPlayerReleased) {
            try {
                player.seekTo(position)
            } catch (e: IllegalStateException) {
                Log.e("PlayerRepositoryImpl", "Error seeking player", e)
            }
        }
    }

    override fun isPrepared(): Boolean {
        return isPrepared
    }
}

/*

class PlayerRepositoryImpl(
    private var player: MediaPlayer,
    private val handler: Handler
) :
    PlayerRepository {


    private var isPrepared: Boolean = false
    private var isPlayerReleased: Boolean = true

    override fun prepare(
        trackUrl: String,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit,
        onError: (what: Int, extra: Int) -> Boolean
    ) {


        try {
            player.apply {
                setDataSource(trackUrl)
                prepareAsync()

                setOnPreparedListener {
                    handler.post {
                        isPrepared = true
                        onPrepared()
                    }
                }

                setOnCompletionListener {
                    handler.post {
                        isPrepared = false
                        onCompletion()
                    }
                }

                setOnErrorListener { _, what, extra ->
                    isPrepared = false
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
        if (isPrepared) {
            player.start()
        }
    }

    override fun pausePlayer() {
        if (player.isPlaying) {
            player.pause()
        }
    }

    override fun stopPlayer() {
        if (isPrepared) {
            player.stop()
            isPrepared = false
        }
    }

    override fun resetPlayer() {
        player.reset()
        isPrepared = false
    }


    override fun releasePlayer() {
        if (!isPlayerReleased) {
            player.release()
            isPrepared = false
            isPlayerReleased = true
        }
    }

    override fun isPlaying(): Boolean {
        return player.isPlaying ?: false
    }

    override fun getCurrentPosition(): Int {
        return player.currentPosition ?: 0
    }

    override fun seekTo(position: Int) {
        player.seekTo(position)
    }

    override fun isPrepared(): Boolean {
        return isPrepared
    }
}
*/
